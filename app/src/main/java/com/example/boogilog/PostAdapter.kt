package com.example.boogilog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boogilog.databinding.FeedItemLayoutBinding
import com.google.firebase.firestore.QueryDocumentSnapshot

data class PostItem(var id : String,
                var nick : String,
                var postHead : String,
                var postBody : String,
                var postImgUrl : String,
                private var postDate : String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id,
                doc["nick"].toString(),
                doc["postHead"].toString(),
                doc["postBody"].toString(),
                doc["postImgUrl"].toString(),
                doc["postDate"].toString())
    constructor(key: String, map: Map<*, *>) :
            this(key,
                map["nick"].toString(),
                map["postHead"].toString(),
                map["postBody"].toString(),
                map["postImgUrl"].toString(),
                map["postDate"].toString(),)

}

class PostAdapter(private val context: HomeFragment, private var postItems: List<PostItem>):
    RecyclerView.Adapter<PostAdapter.PostItemViewHolder>(){
    inner class PostItemViewHolder(val binding: FeedItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val nick = binding.nick
        val postHead = binding.postHead
        val postBody = binding.postBody
        val postImgUrl = binding.postImg



        fun bind(postItem: PostItem, context: Context){
            nick.text = postItem.nick
            postHead.text = postItem.postHead
            postBody.text = postItem.postBody

        }
    }
    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(newList: List<PostItem>) {
        postItems = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FeedItemLayoutBinding = FeedItemLayoutBinding.inflate(inflater, parent, false)
        return PostItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val item = postItems[position]
        holder.binding.nick.text= item.nick
        holder.binding.postHead.text = item.postHead
        holder.binding.postBody.text = item.postBody
    }

    override fun getItemCount(): Int {
        return postItems.size
    }
}