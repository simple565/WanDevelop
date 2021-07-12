package com.maureen.wandevelop.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maureen.wandevelop.databinding.FragmentAccountBinding
import com.maureen.wandevelop.viewmodels.AccountViewModel

class AccountFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}