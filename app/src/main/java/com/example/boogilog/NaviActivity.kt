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


private const val TAG_CALENDER = "calender_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"

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
              
            }
            true
        }


        /*
        notifyBtn.setOnClickListener {
            val intent = Intent(this@NaviActivity, Notify::class.java)
            startActivity(intent)
            println("Notify")
        }
        */
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)

        if (home != null){
            fragTransaction.hide(home)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
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
 