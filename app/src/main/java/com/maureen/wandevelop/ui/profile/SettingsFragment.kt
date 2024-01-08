package com.maureen.wandevelop.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter.ViewBinder
import androidx.preference.PreferenceFragmentCompat
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.BaseFragment
import com.maureen.wandevelop.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment() {

    private lateinit var viewBinding : FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }
}