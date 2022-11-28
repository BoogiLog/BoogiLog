package com.example.boogilog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.inflate
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.boogilog.databinding.ActivityMakeProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.activity_gotopost.*
import kotlinx.android.synthetic.main.activity_make_profile.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MakeProfileActivity : AppCompatActivity(){
    lateinit var nickname:String
    lateinit var profileMessage:String
    lateinit var imageUri:Uri
    var fileName: String? = null
    private val Gallery = 1
    var selectImage: Uri?=null

    lateinit var storage: FirebaseStorage
    val database = FirebaseDatabase.getInstance().reference

    lateinit var firestore :FirebaseFirestore

    val db: FirebaseFirestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()

    val path = auth?.currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMakeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage= FirebaseStorage.getInstance()
        firestore= FirebaseFirestore.getInstance()

        var setProfileBtn = binding.setProfileBtn
        var setNickname = binding.setNickname
        var setProfileMsg = binding.setProfileMsg
        var submitBtn = binding.submitBtn

        ActivityCompat.requestPermissions(this@MakeProfileActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        setProfileBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) //선택하면 무언가를 띄움. 묵시적 호출
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            intent
            launcher.launch(intent)
        }

        submitBtn.setOnClickListener {
            nickname = setNickname.text.toString()
            profileMessage = setProfileMsg.text.toString()

            val userId = nickname
            val photoname = fileName
            val profileMsg = profileMessage
            val following = "0"
            val follower = "0"

            val itemMap = hashMapOf(
                "userId" to userId,
                "imageUri" to photoname,
                "profileMsg" to profileMsg,
                "follower" to follower,
                "following" to following
            )

            val friendMap = hashMapOf(
                "check" to false
            )
            var list: MutableList<String> = mutableListOf()
            db.collection("users").document(path.toString()).set(itemMap)
            db.collection("friends").document(path.toString()).set(friendMap)
            db.collection("friends").get().addOnSuccessListener {

                for (doc in it) {
                    list.add(it.documents.toString())
                    println("Doc : " + doc.id)
                    db.collection(path.toString() + "friends").document(doc.id).set(friendMap)
                }
            }
            if(fileName != null) {
                Toast.makeText(this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, NaviActivity::class.java)
                startActivity(intent)
            }
            else
                Toast.makeText(this, "프로필 사진을 선택하세요.", Toast.LENGTH_SHORT).show()
        }
    }


    var launcher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            imageUri = result.data?.data!!
            if (imageUri != null) {
                uploadFirebase(imageUri)
            }
            imageUri?.let {
                Glide.with(this)
                    .load(imageUri)
                    .into(setProfileBtn)
            }
        }
    }

    private fun uploadFirebase(uri: Uri) {
        var storage: FirebaseStorage? = FirebaseStorage.getInstance()   //FirebaseStorage 인스턴스 생성
        //파일 이름 생성.
        fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"
        //파일 업로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 참조를 생성.
        //참조는 클라우드 파일을 가리키는 포인터라고 할 수 있음.
        var imagesRef = storage!!.reference.child("images/").child(fileName!!)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(baseContext, "success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it)
            Toast.makeText(baseContext, "fail", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Gallery -> {
                    try{
                        selectImage = data?.data
                        setProfileBtn.setImageURI(selectImage)
                        uploadFirebase(selectImage!!)
                    }catch (e:Exception){}
                }
            }
        }
    }


    /*
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
                    .into(setProfileBtn)
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
            Toast.makeText(baseContext, "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it)
            Toast.makeText(baseContext, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
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


    }*/

}