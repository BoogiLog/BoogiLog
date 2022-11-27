package com.example.boogilog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boogilog.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    var navi : NaviActivity?=null

    var items = mutableListOf<PostItem>()
    private var adapter = PostAdapter(this@HomeFragment, items)

    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionPost = db.collection("post")
    private val itemsCollectionFriends = db.collection("friends")
    val auth = FirebaseAuth.getInstance()
    val path = auth?.currentUser?.email

    lateinit var storage: FirebaseStorage
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        navi = NaviActivity()

        updateList()
        binding.listView.layoutManager = LinearLayoutManager(context)
        adapter = PostAdapter(this@HomeFragment, items)
        binding.listView.adapter = adapter

        storage = Firebase.storage

        binding.search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, SearchUserActivity::class.java)
                startActivity(intent)
            }
        })

        binding.write.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, PostWrite::class.java)
                startActivity(intent)
            }
        })

        adapter.setItemClickListener(object : PostAdapter.OnItemClickListener {
            override fun onClickListView(v: View, position: Int) {
                super.onClickListView(v, position)
                println("아이디 : " + adapter.getDocId(position))
                val key = adapter.getDocId(position).toString()
                val intent = Intent(context, GoToPostActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
            }
        })
        //createPost()

        return binding.root
    }

    fun updateList(){
        //itemsCollectionRef.document(path.toString()).collection("posting").get().addOnSuccessListener {
        itemsCollectionPost.get().addOnSuccessListener {
            items = mutableListOf<PostItem>()
            for(doc in it){
                if(doc["nick"].toString() !== path) { // 게시글 쓴 사람이 자신이 아니고
                    itemsCollectionFriends.get().addOnSuccessListener{
                        for (doc2 in it) {
                            if (doc2["check"].toString() === "true") {
                                println("What NICK ? " + doc["nick"].toString())
                                println("What ID ? " + doc2.id)
                                items.add(PostItem(doc))
                            }
                        }
                    }
                }
                //items.add(PostItem(doc))
            }
            adapter?.updateList(items)
        }
    }
    companion object {
        private const val TAG = "HomeFragment"
        fun instance() = HomeFragment()
    }
}