package com.example.boogilog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.boogilog.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_post.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class PostWrite : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    val REQUEST_GET_IMAGE = 105

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("users")
    private val itemsCollectionRef2 = db.collection("post")

    lateinit var storage: FirebaseStorage
    lateinit var firestore:FirebaseFirestore
    var fileName:String? = null

    private var photoUri: Uri? = null

    val auth = FirebaseAuth.getInstance()
    val path = auth?.currentUser?.email

    var document: Int = 0
    var check: Int = 0
    var selectImage: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore= FirebaseFirestore.getInstance()
        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        viewModel.countLiveData.observe(this) {
            document = it
        }


        binding.back.setOnClickListener {
            Toast.makeText(baseContext, "게시물 등록 취소", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

        binding.selectImg.setOnClickListener {
            //navigatePhotos()
            selectGallery()
        }

        binding.submit.setOnClickListener {

            viewModel.increaseCount()
            val postHead = binding.postingHead.text.toString()
            val postBody = binding.postingBody.text.toString()
            var profile : String? = null
            itemsCollectionRef.document(path.toString()).get().addOnSuccessListener {
                profile = it["imageUri"].toString()
            }

            //val postImg = photoUri.toString()
            println("등록 클릭")
            if(fileName != null) {

                val itemMap = hashMapOf(
                    "nick" to  path,
                    "postHead" to postHead,
                    "postBody" to postBody,
                    "profileImgUrl" to profile,
                    "postImgUrl" to fileName,
                    "postDate" to "2022.11.25"
                )
                itemsCollectionRef.document(path.toString()).collection("posting")
                    .document(fileName!!)
                    .set(itemMap)
                itemsCollectionRef2.document(fileName!!).set(itemMap)

                println("ViewModel : " + document)
                Toast.makeText(baseContext, "게시물 등록 완료", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NaviActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(baseContext, "사진을 등록하세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_GET_IMAGE -> {
                    try{
                        photoUri = data?.data
                        binding.postingImg.setImageURI(photoUri)
                        uploadFirebase(photoUri!!)
                    }catch (e:Exception){}
                }
            }
        }
    }

    private fun selectGallery() {

        var writePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var readPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            // 권한 없어서 요청

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1004
            )
        } else {
            // 권한 있음
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GET_IMAGE)
        }
    }

    fun uploadFirebase(uri: Uri) {
        var storage: FirebaseStorage? = FirebaseStorage.getInstance()   //FirebaseStorage 인스턴스 생성
        //파일 이름 생성.
        fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"
        //파일 업로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 참조를 생성.
        //참조는 클라우드 파일을 가리키는 포인터라고 할 수 있음.
        var imagesRef = storage!!.reference.child("/").child(fileName!!)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(baseContext, "Imgage ", Toast.LENGTH_SHORT).show()
            check = 1;
        }.addOnFailureListener {
            println(it)
            Toast.makeText(baseContext, "fail", Toast.LENGTH_SHORT).show()
        }
    }

    /*

    private fun uploadFirebase(){
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
                        var photo = Photo(photoUri.toString(),imageUrl)
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

     */
}