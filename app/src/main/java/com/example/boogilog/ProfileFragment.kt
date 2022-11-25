package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boogilog.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.time.LocalDateTime

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var uid: String
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    val db: FirebaseFirestore = Firebase.firestore
    var userData = mutableListOf<User>()
    var userdata = mutableListOf<User>()
    private var adapter = ProfileAdapter(this@ProfileFragment, userdata)

    private val itemsCollectionRef = db.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //firebase 서비스 초기화

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ProfileAdapter(this@ProfileFragment, userdata)
        binding.recyclerView.adapter = adapter
        updateList()

        val path = auth?.currentUser?.email
        System.out.println(path)
        val path2 = auth?.currentUser?.uid
        System.out.println(path2)

        db.collection("users")
            .document(path2.toString()).get()
            .addOnSuccessListener {
                var follower = it["follower"].toString()
                binding.follower.text = follower

                var following = it["following"].toString()
                binding.following.text = following

                var userId = it["userId"].toString()
                binding.userId.text = userId

                var profileMsg = it["profileMsg"].toString()
                binding.profileMsg.text = profileMsg

                System.out.println(follower)
                System.out.println(following)
                System.out.println(userId)
                System.out.println(profileMsg)

            }

        storage = Firebase.storage
        adapter.setItemClickListener(object : ProfileAdapter.OnItemClickListener {
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
        itemsCollectionRef.get().addOnSuccessListener {
            userdata = mutableListOf<User>()
            for(doc in it){
                userdata.add(User(doc))
            }
            adapter?.updateList(userdata)
        }
    }
    companion object {
        private const val TAG_MY_HOME = "ProfileFragment"
        fun instance() = ProfileFragment()
    }


}