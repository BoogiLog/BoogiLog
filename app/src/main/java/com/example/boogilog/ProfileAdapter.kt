package com.example.boogilog

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.boogilog.databinding.FeedItemLayoutBinding
import com.example.boogilog.databinding.FragmentProfileBinding
import com.example.boogilog.databinding.ProfileItemLayoutBinding
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

data class User (
        var userId: String?=null, /*닉네임*/
        var uid: String?=null, /*uid*/
        var imageUrl: String?=null,  /*사진 다운로드 경로*/
        var follower: String?=null,
        var following: String?=null,
        var profileMsg: String?=null/*프로필 메세지*/,
) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id,
                doc["profileMsg"].toString(),
                doc["userId"].toString(),
                doc["imageUrl"].toString(),
                doc["follower"].toString(),
                doc["following"].toString())

}

class ProfileAdapter(private val context: ProfileFragment, private var User: MutableList<User>):
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    inner class ProfileViewHolder(val binding: FragmentProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val profileMsg = binding.profileMsg
        val userId = binding.userId
        val follower = binding.follower
        val following = binding.following
    }

    /*
    fun interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    */
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClickListView(v: View, position: Int) {

        }
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener

    fun updateList(newList: List<User>) {
        User = newList as MutableList<User>
        notifyDataSetChanged()
    }

    fun getDocId(position: Int): String? {
        return User[position].uid
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.ProfileViewHolder, position: Int) {
        var storage: FirebaseStorage
        storage = Firebase.storage

        //val viewModel = ViewModelProvider()[MyViewModel::class.java]

        val item = User[position]
        println("doc: " + item.uid)
        holder.binding.userId.text = item.userId
        holder.binding.follower.text = item.follower
        holder.binding.following.text = item.following
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


        holder.itemView.setOnClickListener {
            itemClickListener.onClickListView(it, position)
            println("게시물 클릭")
        }
    }

    override fun getItemCount(): Int {
        return User.size
    }
}