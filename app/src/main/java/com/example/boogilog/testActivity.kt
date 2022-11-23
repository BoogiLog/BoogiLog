package com.example.boogilog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class testActivity : AppCompatActivity(), PhotoAdapter.OnItemClickListener {

    lateinit var email: TextView
    lateinit var auth: FirebaseAuth

    lateinit var addPhotoBtn: Button
    lateinit var listRv: RecyclerView

    lateinit var photoAdapter:PhotoAdapter
    lateinit var photoList:ArrayList<Photo>

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.emailtv)
        email.text = auth.currentUser?.email

        firestore= FirebaseFirestore.getInstance()

        addPhotoBtn=findViewById(R.id.add_photo_btn)
        listRv=findViewById(R.id.list_rv)

        photoList= ArrayList()
        photoAdapter=PhotoAdapter(this, photoList)
        listRv.adapter = photoAdapter

        photoAdapter.onItemClickListener=this

        firestore.collection("images").addSnapshotListener {
                querySnapshot, FirebaseFIrestoreException ->

            if (querySnapshot != null) {
                for(dc in querySnapshot) {
                    var photo = dc.toObject(Photo::class.java)
                    photo.name = dc.id
                    photoList.add(photo)
                    System.out.println(dc.id)
                }
            }
            photoAdapter.notifyDataSetChanged()
        }

        addPhotoBtn.setOnClickListener {
            var intent= Intent(this,AddPhotoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(photo: Photo) {
        var intent= Intent(this,PhotoActivity::class.java)
        intent.putExtra("id",photo.name)
        startActivity(intent)
    }
}