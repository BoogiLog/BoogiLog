package com.example.boogilog

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.boogilog.databinding.ActivityGotopostBinding
import com.example.boogilog.databinding.ActivityNaviBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class GoToPostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGotopostBinding
    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("post")
    var result : String? = ""

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            result = data!!.getStringExtra("result") ? : ""
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGotopostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var storage: FirebaseStorage
        storage = Firebase.storage

        val back = binding.back
        back.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }
        //var result = ""
        //val viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        //result = viewModel.key.value.toString()
        //println("파이어베이스 : " + result)
        /*
        viewModel.key.observe(this){
            result = it
        }
         */
        result = intent.getStringExtra("key").toString()
        println("파이어베이스 : " + result)

        itemsCollectionRef.document(result!!).get()
            .addOnSuccessListener {
                binding.nick.text = it["nick"].toString()
                binding.postHead.text = it["postHead"].toString()
                binding.postBody.text = it["postBody"].toString()
                //binding.postImg.setImageBitmap(it["postImgUrl"].toString())

                val imageRef = storage.getReferenceFromUrl(
                    "gs://boogilog-30005.appspot.com/"+it["postImgUrl"].toString()
                )
                displayImageRef(imageRef, binding.postImg)
            }
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }

    companion object {
        private const val TAG = "GoToPostActivity"
        fun instance() = GoToPostActivity()
    }
}