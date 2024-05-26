package com.maureen.wandevelop.feature.bookmark.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.LoadStateFooterAdapter
import com.maureen.wandevelop.databinding.FragmentListBinding
import com.maureen.wandevelop.entity.Bookmark
import com.maureen.wandevelop.feature.bookmark.BookmarkAdapter
import com.maureen.wandevelop.feature.bookmark.BookmarkViewModel
import com.maureen.wandevelop.feature.common.ActionBottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 我的收藏
 * @author lianml
 * @date 2024/5/16
 */
class CollectionFragment : Fragment() {
    companion object {
        private const val TAG = "CollectionFragment"
    }

    private val viewModel: BookmarkViewModel by viewModels<BookmarkViewModel> (ownerProducer = {requireParentFragment()})
    private lateinit var viewBinding: FragmentListBinding
    private val adapter = BookmarkAdapter(this::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        viewBinding.swrl.isRefreshing = true
        viewBinding.swrl.setOnRefreshListener { adapter.refresh() }
        initAdapter()
        observeData()
    }

    private fun initAdapter() {
        viewBinding.rvData.adapter = adapter.withLoadStateFooter(LoadStateFooterAdapter { adapter.retry() })
        adapter.addLoadStateListener {
            viewBinding.swrl.isRefreshing = it.refresh is LoadState.Loading
        }
    }

    private fun onItemClick(view: View, bookmark: Bookmark) {
        when(view.id) {
            R.id.iv_bookmark -> ActionBottomSheetDialogFragment().show(childFragmentManager, ActionBottomSheetDialogFragment.TAG)
        }
    }

    private fun observeData() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            viewModel.loadBookmark().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}