package com.maureen.wandevelop.ui.account

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentRegisterBinding
import com.maureen.wandevelop.network.UserInfo
import com.maureen.wandevelop.viewmodels.AccountViewModel

/**
 * Function:
 *
 * @author lianml
 * Create 2021-07-05
 */
class RegisterFragment : BaseFragment() {
    private val viewModel: AccountViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView() {
        with(viewBinding) {
            registerBtnRegister.setOnClickListener {
                viewModel.register(
                    registerEdtUsername.text.toString(),
                    registerEdtPassword.text.toString()
                )
            }
        }
    }

    override fun initData() {
        viewModel.userInfoLiveData.observe(this, userInfoHandler)
    }

    private val userInfoHandler: (UserInfo) -> Unit = {
        requireActivity().setResult(Activity.RESULT_OK)
        requireActivity().finish()
    }
}