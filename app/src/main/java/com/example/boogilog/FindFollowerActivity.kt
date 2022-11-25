package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boogilog.databinding.FindFollowerBinding

class FindFollowerActivity : AppCompatActivity() {
    private lateinit var binding : FindFollowerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindFollowerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back2.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }
    }
}