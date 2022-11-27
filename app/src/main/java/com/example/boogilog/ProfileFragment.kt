package com.example.boogilog

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.boogilog.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_make_profile.*

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    val auth = FirebaseAuth.getInstance()
    val path = auth?.currentUser?.email
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    val db: FirebaseFirestore = Firebase.firestore
    var userdata = mutableListOf<User>()

    private var adapter = ProfileAdapter(this@ProfileFragment, userdata)
    private val itemsCollectionRef = db.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        updateList()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ProfileAdapter(this@ProfileFragment, userdata)
        binding.recyclerView.adapter = adapter

        storage = Firebase.storage


        var imageUri: String?

        db.collection("users")
            .document(path.toString()).get()
            .addOnSuccessListener {

                var storage :FirebaseStorage= Firebase.storage

                var userId = it["userId"].toString()
                binding.userId.text = userId

                var profileMsg = it["profileMsg"].toString()
                binding.profileMsg.text = profileMsg

                imageUri = it["imageUri"].toString()

                var imageRef2 = storage.getReferenceFromUrl(
                    "gs://boogilog-30005.appspot.com/images/" + imageUri
                )
                displayImageRef(imageRef2, binding.profile)
            }

        return binding.root
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }

    fun updateList(){
        itemsCollectionRef.document(path.toString()).collection("posting").get().addOnSuccessListener {
            userdata = mutableListOf<User>()
            for(doc in it){
                userdata.add(User(doc))
            }
            adapter?.updateList(userdata)
        }
    }

    companion object {
        private const val TAG_MY_HOME = "ProfileFragment"
        fun instance() = ProfileFragment()
    }
}
