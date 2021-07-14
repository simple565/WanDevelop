package com.maureen.wandevelop.viewmodels

import androidx.lifecycle.MutableLiveData
import com.maureen.wandevelop.base.BaseViewModel
import com.maureen.wandevelop.data.HomePageRepository
import com.maureen.wandevelop.network.ArticleBean

class HomeViewModel : BaseViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    val mLiveArticleData = MutableLiveData<MutableList<ArticleBean>>()

    fun topArticleList() = launch(
        block = {
            mLiveArticleData.value = HomePageRepository.loadHomeArticleList(0).datas
        }
    )
}