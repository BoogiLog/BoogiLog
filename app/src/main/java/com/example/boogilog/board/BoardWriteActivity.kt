package com.example.boogilog.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.boogilog.R
import com.example.boogilog.databinding.ActivityBoardWriteBinding
import com.example.boogilog.util.FBAuth
import com.example.boogilog.util.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)

        binding.postBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG, title)
            Log.d(TAG, content)


            val key = FBRef.boardRef.push().key.toString()

            //board
            //  - key
            //      -boardModel(title, content, uid, time)

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title, content, uid, time))

            Toast.makeText(this,"게시글 입력 완료",Toast.LENGTH_SHORT).show()

            imageUpload(key)

            finish()

        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

    }

    private fun imageUpload(key : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainRef.putBytes(data)
        uploadTask.addOnFailureListener() {

        }.addOnSuccessListener { taskSnapshot ->


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }


    }

}