package com.maureen.wandevelop.feature.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
        repository.signIn(userName, password)?.also {
            Log.d(TAG, "signIn: $it")
            val resultMsg = if (it.isSuccess) "登录成功" else it.errorMsg
            _uiState.emit(SignUiState.SignResult(it.isSuccess, resultMsg))
        } ?: run {
            _uiState.emit(SignUiState.SignResult(false, "登录失败，请加网络连接"))
        }
    }

    fun signUp(userName: String, password: String, passwordConfirm: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.signUp(userName, password, passwordConfirm)
    }
}
sealed class SignUiState {

    data object Idle : SignUiState()
    data class SignResult(val result: Boolean, val resultMsg: String) : SignUiState()
}