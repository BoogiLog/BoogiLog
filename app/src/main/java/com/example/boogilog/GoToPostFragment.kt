package com.example.boogilog

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.boogilog.databinding.FragmentGotopostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class GoToPostFragment : Fragment() {
    private lateinit var binding : FragmentGotopostBinding

    //fun interface CallBacks
    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("post")

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var storage: FirebaseStorage
        storage = Firebase.storage

        //val result = arguments?.getString("key").toString()
        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        val result = viewModel.key.toString()
        itemsCollectionRef.document(result).get()
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


        return binding.root
    }

    companion object {
        private const val TAG = "GoToPostFragment"
        fun instance() = GoToPostFragment()
    }
}