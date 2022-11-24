package com.example.boogilog

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.boogilog.databinding.FeedItemLayoutBinding
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

data class PostItem(var id : String,
                    var nick : String,
                    var postHead : String,
                    var postBody : String,
                    var profile: String,
                    var postImgUrl : String,
                    var postDate : String,
                    val like: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id,
                doc["nick"].toString(),
                doc["postHead"].toString(),
                doc["postBody"].toString(),
                doc["profileImgUrl"].toString(),
                doc["postImgUrl"].toString(),
                doc["postDate"].toString(),
                doc["like"].toString())
    constructor(key: String, map: Map<*, *>) :
            this(key,
                map["nick"].toString(),
                map["postHead"].toString(),
                map["postBody"].toString(),
                map["profileImgUrl"].toString(),
                map["postImgUrl"].toString(),
                map["postDate"].toString(),
                map["like"].toString())

}

class PostAdapter(private val context: HomeFragment, private var postItems: List<PostItem>):
    RecyclerView.Adapter<PostAdapter.PostItemViewHolder>(){
    inner class PostItemViewHolder(val binding: FeedItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val nick = binding.nick
        val postHead = binding.postHead
        val postBody = binding.postBody
        val profileImgUrl = binding.profile
        val postImgUrl = binding.postImg
        //val like = binding.like
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClickListView(v: View, position: Int){

        }
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    fun updateList(newList: List<PostItem>) {
        postItems = newList
        notifyDataSetChanged()
    }

    fun getDocId(position: Int): String {
        return postItems[position].id
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }

    private fun displayImageButtonRef(imageRef: StorageReference?, btn: ImageButton) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            btn.setImageBitmap(bmp)
            //view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FeedItemLayoutBinding = FeedItemLayoutBinding.inflate(inflater, parent, false)
        return PostItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        var storage: FirebaseStorage
        storage = Firebase.storage

        //val viewModel = ViewModelProvider()[MyViewModel::class.java]

        val item = postItems[position]
        println("doc: " + item.id)
        holder.binding.nick.text= item.nick
        holder.binding.postHead.text = item.postHead
        holder.binding.postBody.text = item.postBody
        //holder.binding.like = item.like
        //val key = push()
        /*
        holder.binding.like.setOnClickListener {
            println("하트 클릭")
            if(item.like == "0") {
                //item.like.replace("0", "1")
                item.like == "1"
                //updateList(postItems)
            }
            else if(item.like == "1") {
                item.like.replace("1", "0")
                updateList(postItems)
            }
        }

        if(item.like == "0")
            holder.binding.like.setBackgroundResource(R.drawable.unlike)
        else if (item.like == "1")
            holder.binding.like.setBackgroundResource(R.drawable.like)
        */
        val storageRef = storage.reference // reference to root
        val imageRef1 = storage.getReferenceFromUrl(
            "gs://boogilog-30005.appspot.com/"+item.profile
        )

        val imageRef2 = storage.getReferenceFromUrl(
            "gs://boogilog-30005.appspot.com/"+item.postImgUrl
        )
        //holder.binding.profile.background =
        displayImageButtonRef(imageRef1, holder.binding.profile)
        displayImageRef(imageRef2, holder.binding.postImg)

        holder.itemView.setOnClickListener{
            itemClickListener.onClickListView(it, position)
            println("게시물 클릭")
        }
    }

    override fun getItemCount(): Int {
        return postItems.size
    }

}