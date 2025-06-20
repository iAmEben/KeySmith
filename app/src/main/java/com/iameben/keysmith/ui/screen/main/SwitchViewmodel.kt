package com.iameben.keysmith.ui.screen.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SwitchViewmodel @Inject constructor() : ViewModel() {
    private val _switchState = MutableStateFlow(false)
    val switchState: StateFlow<Boolean> = _switchState.asStateFlow()

    fun toggleSwitch() {
        _switchState.value = !_switchState.value
    }

    fun setSwitchState(checked: Boolean) {
        _switchState.value = checked
    }
}