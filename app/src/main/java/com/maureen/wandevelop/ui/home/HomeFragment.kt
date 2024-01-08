package com.maureen.wandevelop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.maureen.wandevelop.adapter.ArticleListAdapter
import com.maureen.wandevelop.databinding.FragmentHomeBinding
import com.maureen.wandevelop.entity.ArticleInfo

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewBinding: FragmentHomeBinding
    private val adapter = ArticleListAdapter(this::onItemClick)

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
        with(viewBinding) {
            homeArticleList.adapter = adapter
            homeSrl.setOnRefreshListener(this@HomeFragment)
        }

    }

    override fun onRefresh() {
    }

    private fun onItemClick(view: View, article: ArticleInfo) {

    }
}