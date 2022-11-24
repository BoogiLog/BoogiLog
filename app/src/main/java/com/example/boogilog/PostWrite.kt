package com.example.boogilog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.boogilog.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_post.*
import java.time.LocalDateTime

class PostWrite : AppCompatActivity() {
    private lateinit var binding : ActivityPostBinding

    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("users")

    lateinit var storage: FirebaseStorage
    private var photoUri: Uri? = null

    var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            Toast.makeText(baseContext, "게시물 등록 취소", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

        println("Check Head : " + binding.postingHead.text.toString())
        println("Check Body : " + binding.postingBody.text.toString())
        //val postImg = "image.jpg"
        //val postDate = LocalDateTime.now().toString()
        val head = binding.postingHead.text.toString()
        val body = binding.postingBody.text.toString()


        //println("postingHead" +postingBody)

        binding.submit.setOnClickListener {
            val postHead = binding.postingHead.text.toString()
            val postBody = binding.postingBody.text.toString()
            println("등록 클릭")
            val itemMap = hashMapOf(
                "nick" to "user1",
                "postHead" to postHead,
                "postBody" to postBody,
                "profileImgUrl" to "image.jpg",
                "postImgUrl" to "achol.png",
                "postDate" to "2022.11.25"
            )
            auth = FirebaseAuth.getInstance()
            val path = auth?.currentUser?.uid
            itemsCollectionRef.document(path.toString()).collection("posting").document("postData3").set(itemMap)
        }


    }
}