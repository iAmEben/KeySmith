package com.iameben.keysmith.ui.screen.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.ui.components.enums.SwitchType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewmodel @Inject constructor(
    private val preferences: AppPreferences
) : ViewModel() {
    private val _switchStates = MutableStateFlow(preferences.getAllSwitchStates())
    val switchStates = _switchStates.asStateFlow()

    fun toggleSwitch(type: SwitchType, isChecked: Boolean) {
        _switchStates.value = _switchStates.value.toMutableMap().apply { this[type] = isChecked }
        preferences.setSwitchState(type, isChecked)
    }
}