package com.maureen.wandevelop.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.BaseDialogFragment
import com.maureen.wandevelop.databinding.FragmentDarkModeSetBinding
import com.maureen.wandevelop.databinding.FragmentProgressBinding

/**
 * 深色模式设置对话框
 * @author lianml
 * @date 2024/2/16
 */
class DarkModeSetDialog : BaseDialogFragment<FragmentDarkModeSetBinding>() {
    companion object {
        const val TAG = "DarkModeSetDialog"
        const val REQUEST_KEY = "requestDarkModeSet"
        const val RESULT_MODE ="resultMode"
    }
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDarkModeSetBinding {
        return FragmentDarkModeSetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> viewBinding.rbDarkModeOn
            AppCompatDelegate.MODE_NIGHT_NO -> viewBinding.rbDarkModeOff
            else -> viewBinding.rbFollowSystem
        }.isChecked = true
        viewBinding.tvConfirm.setOnClickListener {
            val mode = when (viewBinding.rgSet.checkedRadioButtonId) {
                R.id.rb_dark_mode_on -> AppCompatDelegate.MODE_NIGHT_YES
                R.id.rb_dark_mode_off -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            setFragmentResult(REQUEST_KEY, bundleOf(RESULT_MODE to mode))
        }
    }

    override fun getHeightFraction(): Double {
        return 0.0
    }
}