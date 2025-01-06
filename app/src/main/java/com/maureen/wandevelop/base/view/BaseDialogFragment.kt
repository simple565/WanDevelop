package com.maureen.wandevelop.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.maureen.wandevelop.ext.screenHeight
import com.maureen.wandevelop.ext.screenWidth

/**
 * 基础DialogFragment
 * @author lianml
 * Create 2021-07-05
 */
abstract class BaseDialogFragment<T:ViewBinding> : DialogFragment() {
    protected lateinit var viewBinding: T
    override fun onStart() {
        super.onStart()
        dialog?.window?.also {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.setLayout(
                (requireContext().screenWidth * getWidthFraction()).toInt(),
                if (getHeightFraction() == 0.0) ViewGroup.LayoutParams.WRAP_CONTENT else (requireContext().screenHeight * getHeightFraction()).toInt()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = initViewBinding(inflater, container)
        return viewBinding.root
    }

    abstract fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    open fun getWidthFraction(): Double {
        return 0.8
    }

    open fun getHeightFraction(): Double {
        return 0.5
    }
}