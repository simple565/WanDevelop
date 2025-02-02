package com.maureen.wandevelop.feature.profile.sign

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.feature.profile.UserRepository
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @author lianml
 * @date 2024/1/31
 */
class SignViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        private const val TAG = "SignViewModel"
    }
    private val _uiState = MutableStateFlow<SignUiState>(SignUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val repository by lazy {
        UserRepository()
    }

    fun signIn(userName: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        if (userName.isBlank() or password.isBlank()) {
            _uiState.emit(SignUiState.SignResult(false, "用户名或密码为空"))
            return@launch
        }
        val result = repository.signIn(userName, password)
        Log.d(TAG, "signIn: $result")
        if (result.isSuccessWithData) {
            repository.getUserDetailInfo().data?.also {
                UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_DETAIL, Json.encodeToString(it))
            }
            UserPrefUtil.setPreference(UserPrefUtil.KEY_LAST_REQUEST_USER_DETAIL, System.currentTimeMillis())
        }
        val resultMsg = if (result.isSuccess) "登录成功" else result.errorMsg
        _uiState.emit(SignUiState.SignResult(result.isSuccess, resultMsg))
    }

    fun signUp(userName: String, password: String, passwordConfirm: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.signUp(userName, password, passwordConfirm)
    }
}
sealed class SignUiState {

    data object Idle : SignUiState()
    data class SignResult(val result: Boolean, val resultMsg: String) : SignUiState()
}