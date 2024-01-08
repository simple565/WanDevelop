package com.maureen.wandevelop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maureen.wandevelop.R
import com.maureen.wandevelop.adapter.*
import com.maureen.wandevelop.databinding.ActivityMainBinding
import com.maureen.wandevelop.ui.bookmark.BookmarkFragment
import com.maureen.wandevelop.ui.discovery.DiscoveryFragment
import com.maureen.wandevelop.ui.home.HomeFragment
import com.maureen.wandevelop.ui.profile.ProfileFragment


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initView()
    }

    private fun initView() {
        val createFunMap = mapOf(
            R.id.navigation_home to { HomeFragment() },
            R.id.navigation_explore to { DiscoveryFragment() },
            R.id.navigation_bookmark to { BookmarkFragment() },
            R.id.navigation_profile to { ProfileFragment() }
        )
        with(viewBinding) {
            navHostViewPager.isUserInputEnabled = false
            navHostViewPager.adapter = NavPageAdapter(supportFragmentManager, lifecycle, createFunMap)
            bottomNavView.setOnItemSelectedListener {
                navHostViewPager.currentItem = createFunMap.keys.indexOf(it.itemId)
                return@setOnItemSelectedListener true
            }
        }
    }
}