package com.example.boogilog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boogilog.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var adapter: PostAdapter? = null
    var items = mutableListOf<PostItem>()

    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("post")

    lateinit var storage: FirebaseStorage
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val imgBtn = binding.notify

        updateList()
        binding.listView.layoutManager = LinearLayoutManager(context)
        adapter = PostAdapter(this@HomeFragment, items)
        binding.listView.adapter = adapter

        storage = Firebase.storage



        imgBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, Notify::class.java)
                startActivity(intent)
            }
        })

        /*
        // DB 만들기
        val nick = "hansung1"
        val postHead = "어렵다"
        val postBody = "이미지업로드!!!"
        val profileImgUrl = "achol.png"
        val postImgUrl = "image.jpg"
        val postDate = LocalDateTime.now().toString()
        val itemMap = hashMapOf(
            "nick" to nick,
            "postHead" to postHead,
            "postBody" to postBody,
            "profileImgUrl" to profileImgUrl,
            "postImgUrl" to postImgUrl,
            "postDate" to postDate
        )

        itemsCollectionRef.document("test").set(itemMap)

         */
        //createPost()

        return binding.root
    }

    fun createPost(){
        // DB 만들기
        val nick = "hansung1"
        val postHead = "어렵다"
        val postBody = "이미지업로드!!!"
        val profileImgUrl = "achol.png"
        val postImgUrl = "image.jpg"
        val postDate = LocalDateTime.now().toString()
        val itemMap = hashMapOf(
            "nick" to nick,
            "postHead" to postHead,
            "postBody" to postBody,
            "profileImgUrl" to profileImgUrl,
            "postImgUrl" to postImgUrl,
            "postDate" to postDate
        )

        itemsCollectionRef.document("test").set(itemMap)
    }

    fun updateList(){
        itemsCollectionRef.get().addOnSuccessListener {
            items = mutableListOf<PostItem>()
            for(doc in it){
                items.add(PostItem(doc))
            }
            adapter?.updateList(items)
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
        fun instance() = HomeFragment()
    }
}