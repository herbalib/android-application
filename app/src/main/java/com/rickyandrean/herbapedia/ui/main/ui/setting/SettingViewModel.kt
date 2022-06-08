package com.rickyandrean.herbapedia.ui.main.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickyandrean.herbapedia.storage.AuthenticationPreference
import kotlinx.coroutines.launch

class SettingViewModel(private val preference: AuthenticationPreference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}