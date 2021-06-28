package com.maureen.wandevelop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.network.ArticleBean
import com.maureen.wandevelop.network.WanAndroidService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    val mLiveArticleData = MutableLiveData<MutableList<ArticleBean>>()

    fun topArticleList() = viewModelScope.launch {
        val data = WanAndroidService.create().stickyArticleList()
        mLiveArticleData.value = data.data
    }
}