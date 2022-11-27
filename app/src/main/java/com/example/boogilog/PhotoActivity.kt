package com.example.boogilog

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class PhotoActivity : AppCompatActivity() {

    lateinit var imageIv: ImageView
    lateinit var descriptionTv: TextView

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        var id = intent.getStringExtra("id")

        firestore= FirebaseFirestore.getInstance()
        imageIv=findViewById(R.id.imgDES)
        descriptionTv=findViewById(R.id.description)

        if(id!=null){
            firestore.collection("images").document(id).get().addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    var photo=task.result?.toObject(Photo::class.java)
                    Glide.with(this).load(photo?.imageUrl).into(imageIv)
                    descriptionTv.text=photo?.title
                }
            }
        }
    }
}