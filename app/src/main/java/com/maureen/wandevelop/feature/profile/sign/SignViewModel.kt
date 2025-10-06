package com.maureen.wandevelop.feature.profile.sign

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.R
import com.maureen.wandevelop.feature.profile.ProfileRepository
import com.maureen.wandevelop.network.entity.toProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2024/1/31
 */
class SignViewModel() : ViewModel() {
    companion object {
        private const val TAG = "SignViewModel"
    }

    private val repository = ProfileRepository()
    private val _inputState = MutableStateFlow<Map<Int, SignInputState>>(emptyMap())
    val inputState = _inputState.asStateFlow()

    private val _signState = MutableStateFlow<SignState>(SignState())
    val signState = _signState.asStateFlow()

    fun setupInput(keys: List<Int>) {
        _inputState.value = keys.associate { it to SignInputState() }
    }

    fun confirmInput(key: Int, input: String = "") {
        val new = _inputState.value.toMutableMap()
        new[key] = if (input.isBlank()) {
            SignInputState(
                isError = true,
                verifyResult = if (key == R.string.label_username) R.string.prompt_username_is_blank else R.string.prompt_password_is_blank
            )
        } else {
            SignInputState(
                input = input,
                isError = false,
                verifyResult = null
            )
        }
        Log.d(TAG, "confirmInput: ${new.values}")
        _inputState.value = new
    }

    fun toggleInputVisibility(key: Int) {
        val new = _inputState.value.toMutableMap()
        new[key] = if (new.containsKey(key)) {
            new[key]!!.let { it.copy(showInput = it.showInput.not()) }
        } else {
            SignInputState()
        }
        _inputState.value = new
    }

    fun signInOrUp(signIn: Boolean = true) = viewModelScope.launch(Dispatchers.IO) {
        val input = _inputState.value.toMutableMap()
        val userName = input[R.string.label_username]?.input
        val password = input[R.string.label_password]?.input
        if (userName.isNullOrBlank() || password.isNullOrBlank()) {
            return@launch
        }
        val passwordAgain = input[R.string.label_password_again]?.input
        if (signIn.not() && passwordAgain.isNullOrBlank()) {
            return@launch
        }
        _signState.value = SignState(isLoading = true)

        val result = if (signIn) {
            repository.signIn(userName, password)
        } else {
            repository.signUp(userName, password, passwordAgain!!)
        }
        val unreadCount = repository.getUnreadMessageCount().data ?: 0
        if (result.isSuccessWithData) {
            // 保存用户信息
            result.data?.also { repository.saveProfileInfo(it.toProfileInfo().copy(unreadMsgCount = unreadCount)) }
        }
        _signState.value = SignState(result = result.isSuccess, resultMsg = result.errorMsg, isLoading = false)
    }

    fun clearSignState() {
        Log.d(TAG, "clearSignState: ")
        _signState.value = SignState()
    }
}

data class SignInputState(
    val input: String = "",
    val isError: Boolean = false,
    val verifyResult: Int? = null,
    val showInput: Boolean = false
)

data class SignState(
    val result: Boolean = false,
    val resultMsg: String = "",
    val isLoading: Boolean = false
)