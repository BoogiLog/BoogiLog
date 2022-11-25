package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boogilog.databinding.FindFollowingBinding

class FindFollowingActivity : AppCompatActivity() {
    private lateinit var binding: FindFollowingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back2.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }
    }
}