package com.maureen.wandevelop.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.maureen.wandevelop.adapter.HomePageArticleAdapter
import com.maureen.wandevelop.databinding.FragmentHomeBinding
import com.maureen.wandevelop.network.ArticleBean
import com.maureen.wandevelop.viewmodels.HomeViewModel

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewBinding: FragmentHomeBinding

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
        val data = mutableListOf<ArticleBean>()
        val adapter = HomePageArticleAdapter(data)
        with(viewBinding) {
            homeArticleList.layoutManager = LinearLayoutManager(activity)
            homeArticleList.adapter = adapter
            homeSrl.setOnRefreshListener(this@HomeFragment)
        }
        viewModel.mLiveArticleData.observe(viewLifecycleOwner, {
            data.clear()
            data.addAll(it)
            Log.d(TAG, "onViewCreated: ${data.size}")
            adapter.notifyDataSetChanged()
            if (viewBinding.homeSrl.isRefreshing){
                viewBinding.homeSrl.isRefreshing = false
            }
        })
        viewModel.topArticleList()
    }

    override fun onRefresh() {
        viewModel.topArticleList()
    }
}