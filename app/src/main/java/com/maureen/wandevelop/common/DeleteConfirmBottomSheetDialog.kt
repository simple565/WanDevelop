package com.maureen.wandevelop.common

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.BottomSheetDeleteConfirmBinding

/**
 * 删除确认底部弹窗
 * @author lianml
 * @date 2024/5/26
 */
class DeleteConfirmBottomSheetDialog : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ActionBottomSheetDialog"
        const val REQUEST_KEY_CONFIRM_DELETE = "requestKeyConfirmDelete"
        const val RESULT_KEY_CONFIRM_DELETE = "resultConfirmDelete"
    }

    private lateinit var viewBinding: BottomSheetDeleteConfirmBinding

    override fun getTheme(): Int {
        return R.style.AppTheme_Dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BottomSheetDeleteConfirmBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.tvDelete.setOnClickListener { setResult(true) }
        viewBinding.tvCancel.setOnClickListener { setResult(false) }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setResult(false)
    }

    private fun setResult(isDelete: Boolean) {
        setFragmentResult(
            REQUEST_KEY_CONFIRM_DELETE, bundleOf(RESULT_KEY_CONFIRM_DELETE to isDelete)
        )
    }
}