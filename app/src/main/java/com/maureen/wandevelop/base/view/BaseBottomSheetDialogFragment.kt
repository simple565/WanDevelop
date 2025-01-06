package com.maureen.wandevelop.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maureen.wandevelop.ext.screenHeight
import com.maureen.wandevelop.ext.screenWidth

/**
 * 基础BottomSheetDialogFragment
 * @author lianml
 * Create 2021-07-05
 */
abstract class BaseBottomSheetDialogFragment<T:ViewBinding> : BottomSheetDialogFragment() {
    protected lateinit var viewBinding: T
    override fun onStart() {
        super.onStart()
        dialog?.window?.also {
            it.setLayout(requireContext().screenWidth, (requireContext().screenHeight * getHeightFraction()).toInt())
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

    open fun getHeightFraction(): Double {
        return 0.5
    }
}