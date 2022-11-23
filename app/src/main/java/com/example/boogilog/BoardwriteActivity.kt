package com.example.boogilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BoardwriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boardwrite2)

        val database = Firebase.database

        val myRef = database.getReference("Documents")
        myRef.child("test").push().setValue(".")
    }
}