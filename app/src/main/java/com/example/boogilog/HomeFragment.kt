package com.example.boogilog

import android.content.Context
import android.content.Intent
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
import java.time.LocalDateTime


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var thisActivity: NaviActivity
    private var adapter: PostAdapter? = null
    var items = mutableListOf<PostItem>()

    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("post")

    var storage: FirebaseStorage? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        thisActivity = context as NaviActivity
    }

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

        storage = FirebaseStorage.getInstance()

        imgBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, Notify::class.java)
                startActivity(intent)
            }
        })

        // DB 만들기
        val nick = "hansung2"
        val postHead = "성공했다"
        val postBody = "리사이클러뷰!!!!"
        val postImg = "image.jpg"
        val postDate = LocalDateTime.now().toString()
        val itemMap = hashMapOf(
            "nick" to nick,
            "postHead" to postHead,
            "postBody" to postBody,
            "postImgUrl" to postImg,
            "postDate" to postDate
        )

        itemsCollectionRef.document("test2").set(itemMap)

        return binding.root
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
    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.listView.layoutManager = LinearLayoutManager(view.context)


        updateList()
        /*
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<PostItem>()
            for(doc in it){
                items.add(PostItem(doc))
            }
            adapter?.updateList(items)
        }
        */
    }
*/


    companion object {
        private const val TAG = "HomeFragment"
        fun instance() = HomeFragment()
    }
}