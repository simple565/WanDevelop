package com.maureen.wandevelop.common

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.maureen.wandevelop.entity.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author lianml
 * @date 2024/12/22
 */
class ArticleActionViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object {
        private const val TAG = "ArticleActionViewModel"
        const val KEY_ACTION_ARTICLE = "IS_COLLECTED"

    }

    private val article: Article? by lazy {
        state[KEY_ACTION_ARTICLE]
    }
    val isCollected: Boolean
        get() = article?.collect ?: false

    private val repository by lazy { ArticleRepository() }
    private val _uiState =  MutableStateFlow<ActionState>(ActionState.Idle)
    val uiState = _uiState.asStateFlow()

    fun setReadLater() {

    }

    fun getShareIntent(): Intent? {
        return article?.run {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, this.link)
        }
    }
}

sealed class ActionState {
    data object Idle : ActionState()

    data class Result(val isSuccess: Boolean, val msg: String) : ActionState()
}