package com.example.boogilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.boogilog.databinding.Activity


class BoardwriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_post)


}