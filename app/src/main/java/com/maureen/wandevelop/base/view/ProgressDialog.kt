package com.maureen.wandevelop.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.maureen.wandevelop.databinding.FragmentProgressBinding

/**
 * @author lianml
 * @date 2024/2/16
 */
class ProgressDialog: BaseDialogFragment<FragmentProgressBinding>() {
    companion object {
        const val TAG = "ProgressDialog"
        private const val REQUEST_PROGRESS_MSG = "requestProgressMsg"
        @JvmStatic
        fun newInstance(msg: String) = ProgressDialog().also {
            it.arguments = bundleOf(REQUEST_PROGRESS_MSG to msg)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.also {
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProgressBinding {
        return FragmentProgressBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.tvMsg.text = arguments?.getString(REQUEST_PROGRESS_MSG) ?: ""
    }

    override fun getWidthFraction(): Double {
        return 0.5
    }

    override fun getHeightFraction(): Double {
        return 0.2
    }
}