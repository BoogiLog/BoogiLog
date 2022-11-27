package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boogilog.databinding.SearchUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class SearchUserActivity : AppCompatActivity() {
    private lateinit var binding: SearchUserBinding

    private val db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("users")
    var items = mutableListOf<SearchItem>()
    private var adapter = SearchAdapter(this, items)

    val auth = FirebaseAuth.getInstance()
    val path = auth?.currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        println("user? : "+path)

        updateList()
        binding.listView.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this, items)
        binding.listView.adapter = adapter

        binding.back2.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

    }

    fun updateList(){
        val list = itemsCollectionRef.document().id
        println("값 : " +  list)
        db.collection("friends").get().addOnSuccessListener {
            items = mutableListOf<SearchItem>()
            for(doc in it.documents){
                if(doc.id != path)
                    items.add(SearchItem(doc.id, "false"))
            }
            adapter?.updateList(items)
        }
    }
}