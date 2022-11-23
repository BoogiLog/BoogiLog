package com.example.boogilog

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.inflate
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.activity_make_profile.*
import java.text.SimpleDateFormat
import java.util.*

class MakeProfileActivity : AppCompatActivity(){

    lateinit var setProfile: ImageView
    lateinit var setNickname: EditText
    lateinit var setProfileMsg :EditText
    lateinit var submitBtn: Button

    var selectImage: Uri?=null

    lateinit var storage: FirebaseStorage
    lateinit var firestore: FirebaseFirestore
    var database = FirebaseDatabase.getInstance().reference
    var conditionRef = database.child("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_profile)

        storage= FirebaseStorage.getInstance()
        firestore= FirebaseFirestore.getInstance()

        setProfile = findViewById(R.id.setProfile)
        setNickname = findViewById(R.id.setNickname)
        setProfileMsg = findViewById(R.id.setProfileMsg)
        submitBtn = findViewById(R.id.submitBtn)

        ActivityCompat.requestPermissions(this@MakeProfileActivity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        setProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) //선택하면 무언가를 띄움. 묵시적 호출
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            intent
            launcher.launch(intent)
        }



        submitBtn.setOnClickListener (
            ButtonListener()
            )
    }

    inner class ButtonListener: View.OnClickListener {
        override fun onClick(v:View?) {
            when(v?.id) {
                R.id.submitBtn -> conditionRef.setValue(setNickname.text.toString())
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
                    .into(setProfile)
            }
        }
    }


    fun uploadFirebase(uri: Uri) {
        var storage: FirebaseStorage? = FirebaseStorage.getInstance()   //FirebaseStorage 인스턴스 생성
        //파일 이름 생성.
        var fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"

        var imagesRef = storage!!.reference.child("images/").child(fileName)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(baseContext, "success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it)
            Toast.makeText(baseContext, "fail", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart() {
        super.onStart()

        conditionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val text = dataSnapshot
                textView.text = text.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

}