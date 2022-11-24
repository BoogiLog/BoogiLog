package com.example.boogilog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boogilog.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

        val postingHead = binding.postingHead.text
        val postingBody = binding.postingBody.text
        //val postImg = "image.jpg"
        //val postDate = LocalDateTime.now().toString()


        //println("postingHead" +postingBody)

        binding.submit.setOnClickListener {
            println("등록 클릭")
            val itemMap = hashMapOf(
                "postingHead" to postingHead,
                "postingBody" to postingBody
                //"postingImg" to postingImg,
            )
            auth = FirebaseAuth.getInstance()
            val path = auth?.currentUser?.uid
            itemsCollectionRef.document(path.toString()).collection("posting").document("postData").set(itemMap)
        }


    }
}