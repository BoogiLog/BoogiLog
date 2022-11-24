package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.boogilog.databinding.ActivityPostBinding

class PostWrite : AppCompatActivity() {
    private lateinit var binding:ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

        binding.selectImg.setOnClickListener {

        }

    }

}