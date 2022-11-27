package com.example.boogilog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boogilog.databinding.FeedItemLayoutBinding
import com.example.boogilog.databinding.SearchUsersItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

data class SearchItem(var id : String, var follow : String)


class SearchAdapter (private val activity: SearchUserActivity, private var searchItems: List<SearchItem>):
    RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder>(){

    val db: FirebaseFirestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val path = auth?.currentUser?.email


    inner class SearchItemViewHolder(val binding: SearchUsersItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val btn = binding.button;


    }

    fun updateList(newList: List<SearchItem>) {
        searchItems = newList
        println("search " +searchItems.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding: SearchUsersItemBinding = SearchUsersItemBinding.inflate(inflater, parent, false)
        return SearchItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        var storage: FirebaseStorage
        storage = Firebase.storage
        val path = auth?.currentUser?.email
        println("path " + path)

        val item = searchItems[position]
        println("searchItem: " + item.id)
        holder.binding.user.text = item.id

        if(item.follow == "false")
            holder.binding.button.text = "팔로우"
        else
            holder.binding.button.text = "팔로잉"
        val auth = FirebaseAuth.getInstance()
        println("what ? : " + auth.currentUser?.email.toString())
        val test = auth.currentUser?.email
        holder.binding.button.setOnClickListener {
            if(holder.binding.button.text === "팔로잉") {
                holder.binding.button.text = "팔로우"
                val itemMap = hashMapOf(
                    "check" to false
                )
                println("pressed following" + item.id)
                db.collection(test+"friends").document(item.id).set(itemMap)
            }

            else {
                val itemMap = hashMapOf(
                    "check" to true
                )

                println("path : " + path)
                holder.binding.button.text = "팔로잉"
                println("pressed following" + item.id)
                db.collection(test+"friends").document(item.id).set(itemMap)
            }
        }
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }
}