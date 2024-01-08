package com.maureen.wandevelop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Function: ViewPager2适配器
 * @author lianml
 * Create 2021-02-17
 */
class NavPageAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentCreateFunc: Map<Int, () -> Fragment>
) : FragmentStateAdapter(manager, lifecycle) {


    override fun getItemCount(): Int {
        return fragmentCreateFunc.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentCreateFunc.toList().getOrNull(position)?.second?.invoke() ?: throw IndexOutOfBoundsException()
    }
}