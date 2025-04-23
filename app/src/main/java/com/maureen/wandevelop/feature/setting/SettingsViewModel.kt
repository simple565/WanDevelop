package com.maureen.wandevelop.feature.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.entity.OperationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2025/4/14
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingRepository()

    private val _operationState = MutableStateFlow(OperationState())
    val operationState = _operationState.asStateFlow()

    val settingItemsFlow = repository.getSettingItems(application.applicationContext).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = repository.defaultSettingList
    )

    fun updateSetting(nameResId: Int, value: Boolean) = viewModelScope.launch(Dispatchers.Default) {
        repository.updateSetting(nameResId, value)
    }

    fun settingAction(nameResId: Int) = viewModelScope.launch(Dispatchers.Default) {

    }

    fun signOut() = viewModelScope.launch(Dispatchers.Default) {
        _operationState.value = OperationState(
            isOperating = true,
            operatingMsg = MyApplication.instance.applicationContext.getString(R.string.prompt_sign_out_loading)
        )
        val response = repository.signOut()
        if (response.isSuccess) {
            repository.clearProfileCache()
        }
        _operationState.value = OperationState(
            isOperating = false,
            result = response.isSuccess,
            resultMsg = response.errorMsg
        )
    }
}