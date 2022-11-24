package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.boogilog.HomeFragment
import com.example.boogilog.R
import com.example.boogilog.databinding.ActivityNaviBinding
import com.example.boogilog.fragments.SearchFragment

private const val TAG_HOME = "home_fragment"
private const val TAG_SEARCH = "search_fragment"
private const val TAG_PROFILE = "profile_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.searchFragment -> setFragment(TAG_SEARCH, SearchFragment())
                R.id.profileFragment-> setFragment(TAG_PROFILE, ProfileFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val search = manager.findFragmentByTag(TAG_SEARCH)
        val profile = manager.findFragmentByTag(TAG_PROFILE)

        if (home != null){
            fragTransaction.hide(home)
        }

        if(search != null){
            fragTransaction.hide(search)
        }

        if(profile != null){
            fragTransaction.hide(profile)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == TAG_SEARCH) {
            if (search!= null) {
                fragTransaction.show(search)
            }
        }

        else if (tag == TAG_PROFILE) {
            if (profile!= null) {
                fragTransaction.show(profile)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }

    private fun setClick(tag: String, fragment: Fragment){
        /*
        notifyBtn.setOnClickListener {
            val intent = Intent(this@NaviActivity, Notify::class.java)
            startActivity(intent)
            println("Notify")
        }

         */
    }
}
 