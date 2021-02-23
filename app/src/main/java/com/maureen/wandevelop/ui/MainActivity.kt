package com.maureen.wandevelop.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.maureen.wandevelop.R
import com.maureen.wandevelop.adapter.*
import com.maureen.wandevelop.base.BaseActivity
import com.maureen.wandevelop.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var mViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initView()
    }

    override fun initView() {
        with(mViewBinding) {
            with(mainToolbar) {
                setSupportActionBar(this)
                title = getString(R.string.title_home)
            }
            with(mainDrawableLayout) {
                val toggle = ActionBarDrawerToggle(this@MainActivity, mainDrawableLayout, mainToolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close)
                addDrawerListener(toggle)
                toggle.syncState()
                setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            navHostViewPager.adapter = NavPageAdapter(this@MainActivity)
            navHostViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    Log.d(TAG, "onPageScrolled: $position, $positionOffset, $positionOffsetPixels")
                }

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

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.d(TAG, "onPageScrollStateChanged: $state")
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