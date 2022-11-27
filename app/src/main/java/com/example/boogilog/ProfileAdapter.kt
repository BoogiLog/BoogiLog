package com.example.boogilog

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.boogilog.databinding.ProfileItemLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

data class User(var id : String,
                    var nick : String,
                    var postHead : String,
                    var postBody : String,
                    var postDate : String,
                    var postImgUrl : String,
                    var profileImgUrl : String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id,
                doc["nick"].toString(),
                doc["postHead"].toString(),
                doc["postBody"].toString(),
                doc["postDate"].toString(),
                doc["postImgUrl"].toString(),
                doc["profileImgUrl"].toString()
            )
    constructor(key: String, map: Map<*, *>) :
            this(key,
                map["nick"].toString(),
                map["postHead"].toString(),
                map["postBody"].toString(),
                map["postDate"].toString(),
                map["postImgUrl"].toString(),
                map["profileImgUrl"].toString())
}

class ProfileAdapter(private val context: ProfileFragment, private var postItems: List<User>):
    RecyclerView.Adapter<ProfileAdapter.MyPostViewHolder>(){
    inner class MyPostViewHolder(val binding: ProfileItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val postHead = binding.postTitle
        val postBody = binding.postContent
        val postImgUrl = binding.postImage
        //val like = binding.like
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClickListView(v: View, position: Int){

        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun updateList(newList: List<User>) {
        postItems = newList
        notifyDataSetChanged()
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            println("fail")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProfileItemLayoutBinding = ProfileItemLayoutBinding.inflate(inflater, parent, false)
        return MyPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
        var auth: FirebaseAuth? = null
        auth = FirebaseAuth.getInstance()

        var storage :FirebaseStorage= Firebase.storage
        val db: FirebaseFirestore = Firebase.firestore
        val path2 = auth?.currentUser?.email

        val item = postItems[position]
        holder.binding.postTitle.text = item.postHead
        holder.binding.postContent.text = item.postBody
        val imageRef2 = storage.getReferenceFromUrl(
            "gs://boogilog-30005.appspot.com/" + item.postImgUrl
        )
        //holder.binding.profile.background =
        displayImageRef(imageRef2, holder.binding.postImage)

        holder.itemView.setOnClickListener{
            itemClickListener.onClickListView(it, position)
        }
    }

    override fun getItemCount(): Int {
        return postItems.size
    }

}