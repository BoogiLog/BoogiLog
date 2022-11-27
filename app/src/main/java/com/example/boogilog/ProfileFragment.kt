package com.example.boogilog

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

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    val db: FirebaseFirestore = Firebase.firestore
    var userdata = mutableListOf<User>()
    lateinit var imageRef2:String

    private var adapter = ProfileAdapter(this@ProfileFragment, userdata)
    private val itemsCollectionRef = db.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        updateList()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ProfileAdapter(this@ProfileFragment, userdata)
        binding.recyclerView.adapter = adapter

        storage = Firebase.storage

        val path = auth?.currentUser?.email
        System.out.println(path)
        val path2 = auth?.currentUser?.uid
        System.out.println(path2)

        var imageUri: String?

        db.collection("users")
            .document(path.toString()).get()
            .addOnSuccessListener {

                var storage :FirebaseStorage= Firebase.storage

                var follower = it["follower"].toString()
                binding.follower.text = follower

                var following = it["following"].toString()
                binding.following.text = following

                var userId = it["userId"].toString()
                binding.userId.text = userId

                var profileMsg = it["profileMsg"].toString()
                binding.profileMsg.text = profileMsg

                imageUri = it["imageUri"].toString()

                imageRef2 = storage.getReferenceFromUrl(
                    "gs://boogilog-30005.appspot.com/images/" + imageUri
                ).toString()

                Glide.with(this)
                    .load("gs://boogilog-30005.appspot.com/image.jpg")
                    .into(binding.backgroundProfile)

                println(imageRef2)
                println(follower)
                println(following)
                println(userId)
                println(profileMsg)
                println(imageUri)
            }

        return binding.root
    }

    fun updateList(){
        itemsCollectionRef.get().addOnSuccessListener {
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
