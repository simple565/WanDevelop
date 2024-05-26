package com.maureen.wandevelop.feature.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maureen.wandevelop.databinding.FragmentArticleActionBinding

/**
 * @author lianml
 * @date 2024/5/26
 */
class ActionBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ActionBottomSheetDialog"
    }

    private lateinit var viewBinding: FragmentArticleActionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentArticleActionBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}