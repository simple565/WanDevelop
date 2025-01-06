package com.maureen.wandevelop.feature.profile.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.view.ProgressDialog
import com.maureen.wandevelop.databinding.FragmentSignInBinding
import com.maureen.wandevelop.feature.profile.SignUiState
import com.maureen.wandevelop.feature.profile.SignViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * 登录页面
 *
 * @author lianml
 * Create 2021-07-05
 */
class SignInFragment : Fragment() {
    private lateinit var viewBinding: FragmentSignInBinding
    private val viewModel: SignViewModel by activityViewModels()
    private val progressDialog by lazy { ProgressDialog.newInstance("登录中……") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSignInBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        observeData()
    }

    private fun initView() {
        viewBinding.tvForgotPassword.setOnClickListener {
            Snackbar.make(viewBinding.root, "暂不支持", Snackbar.LENGTH_SHORT)
                .setAction(R.string.confirm) {}
                .show()
        }
        viewBinding.btnLogin.setOnClickListener {
            progressDialog.show(childFragmentManager, ProgressDialog.TAG)
            viewModel.signIn(viewBinding.edtUsername.text.toString(), viewBinding.edtPassword.text.toString())
        }
        viewBinding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_to_register)
        }
    }

    private fun observeData() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.uiState.collectLatest {
                when(it) {
                    is SignUiState.SignResult -> {
                        progressDialog.dismissAllowingStateLoss()
                        Toast.makeText(requireContext(), it.resultMsg, Toast.LENGTH_SHORT).show()
                        if (it.result) {
                            activity?.setResult(Activity.RESULT_OK)
                            activity?.finish()
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}