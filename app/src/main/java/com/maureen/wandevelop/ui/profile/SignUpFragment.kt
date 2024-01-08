package com.maureen.wandevelop.ui.profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentSignUpBinding
import com.maureen.wandevelop.entity.UserInfo

/**
 * Function:
 *
 * @author lianml
 * Create 2021-07-05
 */
class SignUpFragment : BaseFragment() {
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView() {
        viewBinding.btnSignUp.setOnClickListener {
        }
    }

    override fun initData() {
        //viewModel.userInfoLiveData.observe(this, userInfoHandler)
    }

    private val userInfoHandler: (UserInfo) -> Unit = {
        requireActivity().setResult(Activity.RESULT_OK)
        requireActivity().finish()
    }
}