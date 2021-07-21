package com.maureen.wandevelop.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentLoginBinding
import com.maureen.wandevelop.network.UserInfo
import com.maureen.wandevelop.viewmodels.AccountViewModel


/**
 * Function:
 *
 * @author lianml
 * Create 2021-07-05
 */
class LoginFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentLoginBinding
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView() {
        with(viewBinding) {
            loginTvGoRegister.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }
            loginBtnLogin.setOnClickListener {
                viewModel.login(loginEdtUsername.text.toString(), loginEdtPassword.text.toString())
            }
        }
    }

    override fun initData() {
        viewModel.userInfoLiveData.observe(this, userInfoHandler)
    }

    private val userInfoHandler: (UserInfo) -> Unit = {
        requireActivity().finish()
    }
}