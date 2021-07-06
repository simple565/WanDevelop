package com.maureen.wandevelop.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.maureen.wandevelop.adapter.HomePageArticleAdapter
import com.maureen.wandevelop.databinding.FragmentHomeBinding
import com.maureen.wandevelop.network.ArticleBean
import com.maureen.wandevelop.viewmodels.HomeViewModel

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private val mViewModel: HomeViewModel by viewModels()
    private lateinit var mViewBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(mViewBinding) {
            val data = mutableListOf<ArticleBean>()
            val adapter = HomePageArticleAdapter(data)
            homeArticleList.layoutManager = LinearLayoutManager(activity)
            homeArticleList.adapter = adapter
            mViewModel.mLiveArticleData.observe(viewLifecycleOwner, {
                data.clear()
                data.addAll(it)
                Log.d(TAG, "onViewCreated: ${data.size}")
                adapter.notifyDataSetChanged()
            })
            mViewModel.topArticleList()
        }
    }
}