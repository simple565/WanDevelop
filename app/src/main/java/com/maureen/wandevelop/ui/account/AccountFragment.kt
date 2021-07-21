package com.maureen.wandevelop.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentAccountBinding
import com.maureen.wandevelop.viewmodels.AccountViewModel

class AccountFragment : BaseFragment(), NestedScrollView.OnScrollChangeListener {
    private var isToolBarShown = false
    private val viewModel: AccountViewModel by viewModels()

    private lateinit var viewBinding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAccountBinding.inflate(inflater, container, false)
        return viewBinding.root
    }


    override fun initView() {
        with(viewBinding) {
            accountScroll.setOnScrollChangeListener(this@AccountFragment)
            accountTvNickname.setOnClickListener {
                startActivity(
                    Intent(
                        requireActivity(),
                        LoginActivity::class.java
                    )
                )
            }
            accountSwcDarkMode.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            accountBtnLogout.setOnClickListener { viewModel.logout() }
        }
    }

    override fun initData() {

    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val showToolBar = scrollY > viewBinding.accountAppbar.height
        if (isToolBarShown != showToolBar) {
            isToolBarShown = showToolBar
        }
        viewBinding.accountAppbar.isActivated = isToolBarShown
        viewBinding.accountToolbarLayout.isTitleEnabled = isToolBarShown
    }
}