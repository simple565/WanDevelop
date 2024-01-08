package com.maureen.wandevelop.ui.profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentSignInBinding
import com.maureen.wandevelop.entity.UserInfo


/**
 * Function:登录页面
 *
 * @author lianml
 * Create 2021-07-05
 */
class SignInFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentSignInBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSignInBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView() {
        viewBinding.tvForgotPassword.setOnClickListener {
            Snackbar.make(viewBinding.root, "暂不支持", Snackbar.LENGTH_SHORT)
                .setAction(R.string.confirm) {}
                .show()
        }
        viewBinding.btnLogin.setOnClickListener {
            //viewModel.login(viewBinding.edtUsername.text.toString(), viewBinding.edtPassword.text.toString())
        }
        viewBinding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_to_register)
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