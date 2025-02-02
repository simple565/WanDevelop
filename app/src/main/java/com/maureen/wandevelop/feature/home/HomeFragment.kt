package com.maureen.wandevelop.feature.home

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
import com.maureen.wandevelop.R
import com.maureen.wandevelop.adapter.ArticleAdapter
import com.maureen.wandevelop.adapter.BannerAdapter
import com.maureen.wandevelop.databinding.FragmentHomeBinding
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.ext.startActivity
import com.maureen.wandevelop.common.ArticleActionDialog
import com.maureen.wandevelop.feature.discovery.SearchActivity
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var viewBinding: FragmentHomeBinding
    private val articleAdapter = ArticleAdapter(this::onArticleItemClick)
    private val bannerAdapter: BannerAdapter = BannerAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        viewBinding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_search) {
                requireContext().startActivity(SearchActivity::class.java)
            }
            return@setOnMenuItemClickListener true
        }
        viewBinding.banner.setAdapter(bannerAdapter)
            .addBannerLifecycleObserver(viewLifecycleOwner)
            .setIndicator(CircleIndicator(requireContext()))
        viewBinding.rvArticle.adapter = articleAdapter
    }

    private fun observeData() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.uiState.onEach {
                when (it) {
                    is HomeUiState.LoadBannerResult -> {
                        bannerAdapter.setDatas(it.bannerData)
                    }
                    else -> {}
                }
            }.launchIn(this)

            viewModel.articleFlow.onEach {
                articleAdapter.submitData(it)
            }.launchIn(this)
        }
    }

    private fun onArticleItemClick(view: View, article: Article) {
        Log.d(TAG, "onArticleItemClick: ${article.id}")
        if (view.id == R.id.iv_more_action) {
            ArticleActionDialog.newInstance(article)
                .show(childFragmentManager, ArticleActionDialog.TAG)
        } else {

        }
    }
}