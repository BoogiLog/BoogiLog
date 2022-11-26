package com.example.boogilog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boogilog.databinding.FeedItemLayoutBinding
import com.example.boogilog.databinding.SearchUsersItemBinding
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

data class SearchItem(var id : String, var follow : Boolean)


class SearchAdapter (private val activity: SearchUserActivity, private var searchItems: List<SearchItem>):
    RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder>(){
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

        val item = searchItems[position]
        println("searchItem: " + item.id)
        holder.binding.user.text = item.id
        if(item.follow == false)
            holder.binding.button.text = "팔로우"
        else
            holder.binding.button.text = "팔로잉"

        holder.binding.button.setOnClickListener{
            if(holder.binding.button.text === "팔로잉")
                holder.binding.button.text = "팔로우"
            else
                holder.binding.button.text = "팔로잉"
        }
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }
}