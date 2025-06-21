package com.iameben.keysmith.ui.screen.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import com.iameben.keysmith.ui.components.enums.SwitchType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SwitchViewmodel @Inject constructor() : ViewModel() {
    private val _switchStates = MutableStateFlow(mapOf(
        SwitchType.RANDOM to false,
        SwitchType.SMART to false,
        SwitchType.UPPERCASE to false,
        SwitchType.LOWERCASE to false,
        SwitchType.NUMBERS to false,
        SwitchType.SPECIAL_CHARS to false
    ))
    val switchStates = _switchStates.asStateFlow()

    fun toggleSwitch(type: SwitchType, isChecked: Boolean) {
        _switchStates.value = _switchStates.value.toMutableMap().apply { this[type] = isChecked }
    }
}