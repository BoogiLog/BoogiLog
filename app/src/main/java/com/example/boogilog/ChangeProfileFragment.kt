package com.example.boogilog;

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import kotlinx.android.synthetic.main.activity_change_profile.*

class ChangeProfileFragment : AppCompatActivity() {

    lateinit var bitmap: Bitmap
    lateinit var backgrdImg : ImageView

    val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if(!it.value) {
                Toast.makeText(applicationContext, "권한 동의 필요!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        profileImg.load(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        checkPermission.launch(permissionList)

        galleryBtn.setOnClickListener {
            readImage.launch("image/*")
        }

        cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResult.launch(intent)
        }

    }

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK && it.data!=null) {
            val extras = it.data!!.extras
            bitmap = extras?.get("data") as Bitmap
            backgrdImg.setImageBitmap(bitmap)
        }
    }
}

