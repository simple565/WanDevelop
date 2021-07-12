package com.maureen.wandevelop.ui

import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.maureen.wandevelop.R
import com.maureen.wandevelop.adapter.*
import com.maureen.wandevelop.base.BaseActivity
import com.maureen.wandevelop.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {
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

    override fun initView() {
        with(viewBinding) {
            navHostViewPager.adapter = NavPageAdapter(this@MainActivity)
            navHostViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "onPageSelected: $position")
                    when (position) {
                        HOME_PAGE_INDEX -> mainBottomNavView.selectedItemId = R.id.navigation_home
                        DISCOVERY_PAGE_INDEX -> mainBottomNavView.selectedItemId = R.id.navigation_discovery
                        ACCOUNT_PAGE_INDEX -> mainBottomNavView.selectedItemId = R.id.navigation_account
                        KNOWLEDGE_PAGE_INDEX -> mainBottomNavView.selectedItemId = R.id.navigation_knowledge
                        else -> mainBottomNavView.selectedItemId = R.id.navigation_home
                    }
                }
            })
            mainBottomNavView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_home -> navHostViewPager.currentItem = HOME_PAGE_INDEX
                    R.id.navigation_discovery -> navHostViewPager.currentItem = DISCOVERY_PAGE_INDEX
                    R.id.navigation_account -> navHostViewPager.currentItem = ACCOUNT_PAGE_INDEX
                    R.id.navigation_knowledge -> navHostViewPager.currentItem = KNOWLEDGE_PAGE_INDEX
                    else -> navHostViewPager.currentItem = HOME_PAGE_INDEX
                }
                true
            }
        }
    }
}