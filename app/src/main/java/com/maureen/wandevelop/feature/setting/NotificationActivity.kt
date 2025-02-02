package com.maureen.wandevelop.feature.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.maureen.wandevelop.base.view.LoadStateFooterAdapter
import com.maureen.wandevelop.databinding.AcitivityNotificationBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "NotificationActivity"
    }

    private val viewBinding by lazy {
        AcitivityNotificationBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<NotificationViewModel>()
    private val adapter = NotificationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.toolbar.setNavigationOnClickListener { finish() }
        viewBinding.swrl.isRefreshing = true
        viewBinding.swrl.setOnRefreshListener { adapter.refresh() }
        initAdapter()
        observeData()
    }

    private fun initAdapter() {
        viewBinding.toolbar.setNavigationOnClickListener { finish() }
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        viewBinding.rvMessage.addItemDecoration(decoration)
        viewBinding.rvMessage.adapter = adapter.withLoadStateFooter(LoadStateFooterAdapter { adapter.retry() })
        adapter.addLoadStateListener {
            viewBinding.swrl.isRefreshing = it.refresh is LoadState.Loading
        }
    }
    private fun observeData() = lifecycleScope.launch {
        viewModel.deferred.await()
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.messageFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}