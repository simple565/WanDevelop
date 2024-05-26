package com.maureen.wandevelop.feature.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maureen.wandevelop.databinding.FragmentSignUpBinding
import com.maureen.wandevelop.feature.profile.SignViewModel

/**
 * 注册页面
 *
 * @author lianml
 * Create 2021-07-05
 */
class SignUpFragment : Fragment() {
    private val viewModel: SignViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.btnSignUp.setOnClickListener {
            viewModel.signUp(
                viewBinding.edtUsername.toString(),
                viewBinding.edtPassword.toString(),
                viewBinding.edtPasswordConfirm.toString()
            )
        }
        viewBinding.tvSignIn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}