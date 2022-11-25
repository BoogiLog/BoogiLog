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
    private val itemsCollectionRef = db.collection("users")

    val auth = FirebaseAuth.getInstance()
    val path = auth?.currentUser?.uid

    lateinit var storage: FirebaseStorage
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val imgBtn = binding.write
        navi = NaviActivity()

        updateList()
        binding.listView.layoutManager = LinearLayoutManager(context)
        adapter = PostAdapter(this@HomeFragment, items)
        binding.listView.adapter = adapter

        storage = Firebase.storage



        imgBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, PostWrite::class.java)
                startActivity(intent)
            }
        })

        adapter.setItemClickListener(object : PostAdapter.OnItemClickListener {
            override fun onClickListView(v: View, position: Int) {
                super.onClickListView(v, position)
                //val intent = Intent(context, GoToPostFragment::class.java)
                //startActivity(intent)
                //viewModel.setKey("test")
                //navi!!.fragmentChange(1)
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
        itemsCollectionRef.document(path.toString()).collection("posting").get().addOnSuccessListener {
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