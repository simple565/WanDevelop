package com.maureen.wandevelop.common

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.view.BaseDialogFragment
import com.maureen.wandevelop.common.ArticleActionViewModel.Companion.KEY_ACTION_ARTICLE
import com.maureen.wandevelop.databinding.FragmentArticleActionBinding
import com.maureen.wandevelop.entity.Article
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * @author lianml
 * @date 2024/5/26
 */
class ArticleActionDialog : BaseDialogFragment<FragmentArticleActionBinding>() {
    companion object {
        const val TAG = "ArticleActionDialog"
        @JvmStatic
        fun newInstance(article: Article) = ArticleActionDialog().also {
            it.arguments = bundleOf(KEY_ACTION_ARTICLE to article)
        }
    }

    private val viewModel by viewModels<ArticleActionViewModel>()

    override fun getTheme(): Int {
        return R.style.AppTheme_Dialog
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentArticleActionBinding {
        return FragmentArticleActionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun getHeightFraction(): Double {
        return 0.16
    }

    private fun initView() {
        viewBinding.tvReadLater.setOnClickListener {
            viewModel.setReadLater()
        }
        viewBinding.tvShare.setOnClickListener {
            startActivity(Intent.createChooser(viewModel.getShareIntent(), "分享到"))
        }
    }

    private fun observeData() {
        viewModel.uiState.onEach {
            when(it) {
                is  ActionState.Result -> {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    dismissAllowingStateLoss()
                }
                ActionState.Idle -> {}
            }
        }.launchIn(lifecycleScope)
    }
}