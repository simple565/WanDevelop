package com.maureen.wandevelop.ui.profile

import androidx.lifecycle.ViewModel
import com.maureen.wandevelop.network.RetrofitManager

/**
 * Function: 我的页面 ViewModel
 * @author lianml
 * Create 2021-07-11
 */
class ProfileViewModel : ViewModel() {
    init {
        if (RetrofitManager.hasCookie()) {

        }
    }
}