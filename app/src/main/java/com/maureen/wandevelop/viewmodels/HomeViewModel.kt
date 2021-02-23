package com.maureen.wandevelop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.network.ArticleBean
import com.maureen.wandevelop.network.WanAndroidService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    val mLiveArticleData = MutableLiveData<MutableList<ArticleBean>>()

    fun topArticleList() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                WanAndroidService.create().topArticleList()
            }
            mLiveArticleData.value = data.data
        }
    }
}