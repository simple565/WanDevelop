package com.maureen.wandevelop.feature.bookmark.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.maureen.wandevelop.R
import com.maureen.wandevelop.adapter.NavPageAdapter
import com.maureen.wandevelop.databinding.FragmentBookmarkBinding
import com.maureen.wandevelop.feature.bookmark.BookmarkViewModel

class BookmarkFragment : Fragment() {
    companion object {
        private const val TAG = "BookmarkFragment"
    }

    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var viewBinding: FragmentBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val tabText = listOf(getString(R.string.nav_my_bookmark), getString(R.string.nav_read_later))
        val createFunList = listOf({ CollectionFragment() }, { ReadLaterFragment() })
        viewBinding.vpBookmark.adapter = NavPageAdapter(childFragmentManager, lifecycle, createFunList)
        TabLayoutMediator(
            viewBinding.tbl,
            viewBinding.vpBookmark
        ) { tab, position ->
            Log.d(TAG, "onViewCreated: show $position")
            tab.text = tabText[position]
        }.attach()
    }
}