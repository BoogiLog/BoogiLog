package com.example.boogilog

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.common.io.Files.getFileExtension
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.activity_change_profile.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddPhotoActivity : AppCompatActivity() {

    lateinit var imageIv: ImageView
    lateinit var textEt: EditText
    lateinit var uploadBtn: Button

    val IMAGE_PICK = 1111

    var selectImage: Uri?=null

    lateinit var storage: FirebaseStorage
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        imageIv = findViewById(R.id.image_iv)
        textEt = findViewById(R.id.textEdit)
        uploadBtn = findViewById(R.id.uploadBtn)

        ActivityCompat.requestPermissions(this@AddPhotoActivity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        imageIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) //선택하면 무언가를 띄움. 묵시적 호출
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            intent
            launcher.launch(intent)
        }

        uploadBtn.setOnClickListener {
            if(selectImage!=null) {
                var fileName =
                    SimpleDateFormat("yyyyMMddHHmmss").format(Date()) // 파일명이 겹치면 안되기 떄문에 시년월일분초 지정
                storage.getReference().child("images").child(fileName)
                    .putFile(selectImage!!)//어디에 업로드할지 지정
                    .addOnSuccessListener {
                            taskSnapshot -> // 업로드 정보를 담는다
                        taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                                it->
                            var imageUrl=it.toString()
                            var photo = Photo(textEt.text.toString(),imageUrl)
                            firestore.collection("images")
                                .document().set(photo)
                                .addOnSuccessListener {
                                    finish()
                                }
                        }
                    }
            } else {
                Toast.makeText(baseContext, "사진선택해", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var launcher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                uploadFirebase(imageUri)
            }
            imageUri?.let {
                 Glide.with(this)
                    .load(imageUri)
                    .into(imageIv)
            }
        }
    }


    fun uploadFirebase(uri: Uri) {
        var storage: FirebaseStorage? = FirebaseStorage.getInstance()   //FirebaseStorage 인스턴스 생성
        //파일 이름 생성.
        var fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"
        //파일 업로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 참조를 생성.
        //참조는 클라우드 파일을 가리키는 포인터라고 할 수 있음.
        var imagesRef = storage!!.reference.child("images/").child(fileName)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(baseContext, "success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it)
            Toast.makeText(baseContext, "fail", Toast.LENGTH_SHORT).show()
        }
    }
}