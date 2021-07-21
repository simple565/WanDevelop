package com.maureen.wandevelop.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Function:
 *
 * @author lianml
 * Create 2021-07-05
 */
abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    protected abstract fun initView()

    protected abstract fun initData()
}