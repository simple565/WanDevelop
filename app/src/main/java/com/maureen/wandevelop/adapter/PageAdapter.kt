package com.maureen.wandevelop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * ViewPager2适配器
 * @author lianml
 * @date 2021-02-17
 */
class NavPageAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentCreateFunc: List<() -> Fragment>
) : FragmentStateAdapter(manager, lifecycle) {


    override fun getItemCount(): Int {
        return fragmentCreateFunc.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentCreateFunc.getOrNull(position)?.invoke() ?: throw IndexOutOfBoundsException()
    }
}