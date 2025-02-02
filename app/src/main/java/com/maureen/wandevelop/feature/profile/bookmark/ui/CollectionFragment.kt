package com.maureen.wandevelop.feature.profile.bookmark.ui

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
import androidx.paging.PagingData
import com.maureen.wandevelop.base.view.LoadStateFooterAdapter
import com.maureen.wandevelop.databinding.FragmentListBinding
import com.maureen.wandevelop.entity.Collection
import com.maureen.wandevelop.ext.showAndRequest
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkAdapter
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkViewModel
import com.maureen.wandevelop.common.DeleteConfirmBottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
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
    private val adapter = BookmarkAdapter(this::onItemLongClick)

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
        viewBinding.swrl.isRefreshing = true
        viewBinding.swrl.setOnRefreshListener { adapter.refresh() }
        initAdapter()
        observeData()
    }

    private fun initAdapter() {
        viewBinding.rvData.adapter = adapter.withLoadStateFooter(LoadStateFooterAdapter (retry = { adapter.retry() }))
        adapter.addLoadStateListener {
            viewBinding.swrl.isRefreshing = it.refresh is LoadState.Loading
            if (adapter.itemCount != 0) {
                viewBinding.rvData.smoothScrollToPosition(0)
            }
        }
    }

    private fun onItemLongClick(bookmark: Collection) {
        deleteArticleOrNot(bookmark)
    }

    private fun observeData() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            viewModel.collectionFlow.collectLatest {
                adapter.submitData(PagingData.empty())
                adapter.submitData(it)
            }
        }
    }

    private fun deleteArticleOrNot(collection: Collection) = viewLifecycleOwner.lifecycleScope.launch {
        val result = DeleteConfirmBottomSheetDialog().showAndRequest(
            childFragmentManager,
            DeleteConfirmBottomSheetDialog.TAG,
            DeleteConfirmBottomSheetDialog.REQUEST_KEY_CONFIRM_DELETE,
            viewLifecycleOwner
        ).firstOrNull()?.getBoolean(DeleteConfirmBottomSheetDialog.RESULT_KEY_CONFIRM_DELETE) ?: false
        Log.d(TAG, "deleteArticleOrNot: $result")
        if (!result) {
            return@launch
        }

        val removeIndex = viewModel.cancelCollect(adapter.snapshot(), collection)
        Log.d(TAG, "deleteArticleOrNot: $removeIndex")
        adapter.notifyItemRemoved(removeIndex)
    }
}