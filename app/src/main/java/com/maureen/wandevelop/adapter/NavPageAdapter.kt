package com.maureen.wandevelop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maureen.wandevelop.ui.AccountFragment
import com.maureen.wandevelop.ui.DiscoveryFragment
import com.maureen.wandevelop.ui.HomeFragment
import com.maureen.wandevelop.ui.KnowledgeFragment

/**
 * Function:
 * @author lianml
 * Create 2021-02-17
 */

const val HOME_PAGE_INDEX = 0
const val KNOWLEDGE_PAGE_INDEX = 1
const val DISCOVERY_PAGE_INDEX = 2
const val ACCOUNT_PAGE_INDEX = 3

class NavPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val navFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
            HOME_PAGE_INDEX to { HomeFragment() },
            DISCOVERY_PAGE_INDEX to { DiscoveryFragment() },
            KNOWLEDGE_PAGE_INDEX to { KnowledgeFragment() },
            ACCOUNT_PAGE_INDEX to { AccountFragment() }
    )

    override fun getItemCount(): Int {
        return navFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return navFragmentsCreators[position]?.invoke()?: throw IndexOutOfBoundsException()
    }
}