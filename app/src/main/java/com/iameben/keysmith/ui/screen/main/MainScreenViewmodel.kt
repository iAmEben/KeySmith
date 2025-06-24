package com.iameben.keysmith.ui.screen.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.ui.components.enums.ModeSelector
import com.iameben.keysmith.ui.components.enums.SwitchType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewmodel @Inject constructor(
    private val preferences: AppPreferences
) : ViewModel() {
    private val _switchStates = MutableStateFlow(preferences.getAllSwitchStates())
    val switchStates = _switchStates.asStateFlow()

    private val _sliderValue = MutableStateFlow(preferences.getSliderValue())
    val sliderValue = _sliderValue.asStateFlow()

    private val _selectMode = MutableStateFlow(preferences.getMode())
    val selectMode = _selectMode.asStateFlow()

    fun toggleSwitch(type: SwitchType, isChecked: Boolean) {
        _switchStates.value = _switchStates.value.toMutableMap().apply { this[type] = isChecked }
        preferences.setSwitchState(type, isChecked)
    }

    fun isSwitchOn(type: SwitchType): Boolean = switchStates.value[type] == true

    fun setSliderValue(value: Int) {
        _sliderValue.value = value
        preferences.setSliderValue(value)
    }

    fun setSelectMode(mode: ModeSelector) {
        _selectMode.value = mode
        preferences.setMode(mode)
    }


}