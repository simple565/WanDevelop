package com.maureen.wandevelop.feature.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2025/4/14
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingRepository()

    val settingItemsFlow = repository.getSettingItems(application.applicationContext).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = repository.defaultSettingList
    )

    fun updateSetting(nameResId: Int, value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSetting(nameResId, value)
    }

    fun settingAction(nameResId: Int) = viewModelScope.launch(Dispatchers.IO) {

    }
}