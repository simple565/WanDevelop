package com.maureen.wandevelop.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentRegisterBinding

/**
 * Function:
 *
 * @author lianml
 * Create 2021-07-05
 */
class RegisterFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}