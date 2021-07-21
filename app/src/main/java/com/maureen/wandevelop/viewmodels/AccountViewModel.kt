package com.maureen.wandevelop.viewmodels

import androidx.lifecycle.MutableLiveData
import com.maureen.wandevelop.base.BaseViewModel
import com.maureen.wandevelop.data.AccountRepository
import com.maureen.wandevelop.network.UserInfo

/**
 * Function:
 * @author lianml
 * Create 2021-07-11
 */
class AccountViewModel : BaseViewModel() {

    val userInfoLiveData = MutableLiveData<UserInfo>()

    fun login(username: String, password: String) = launch(
        block = {
            userInfoLiveData.value = AccountRepository.login(username, password).apiData()
        }
    )

    fun logout() = launch(
        block = {
            AccountRepository.logout()
        }
    )
}