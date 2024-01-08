package com.maureen.wandevelop.base

import androidx.lifecycle.ViewModel
import com.maureen.wandevelop.network.RetrofitManager

/**
 * @author lianml
 * @date 2024/1/2
 */
class BaseViewModel: ViewModel() {
    protected fun isLogin() : Boolean{
        return RetrofitManager.hasCookie()
    }
}