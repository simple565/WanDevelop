package com.maureen.wandevelop.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentLoginBinding


/**
 * Function:
 *
 * @author lianml
 * Create 2021-07-05
 */
class LoginFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.loginTvGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }
}