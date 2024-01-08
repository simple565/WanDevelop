package com.maureen.wandevelop.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentProfileBinding

/**
 * 我的页面
 */
class ProfileFragment : BaseFragment() {
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var viewBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return viewBinding.root
    }


    override fun initView() {

    }

    override fun initData() {

    }

}