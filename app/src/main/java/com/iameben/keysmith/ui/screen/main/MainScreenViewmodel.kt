package com.iameben.keysmith.ui.screen.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.ui.components.enums.ModeSelector
import com.iameben.keysmith.ui.components.enums.SwitchType
import com.iameben.keysmith.util.PasswordGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewmodel @Inject constructor(
    private val passwordGenerator: PasswordGenerator,
    private val preferences: AppPreferences
) : ViewModel() {
    private val _switchStates = MutableStateFlow(preferences.getAllSwitchStates())
    val switchStates = _switchStates.asStateFlow()

    private val _sliderValue = MutableStateFlow(preferences.getSliderValue())
    val sliderValue = _sliderValue.asStateFlow()

    private val _selectMode = MutableStateFlow(preferences.getMode())
    val selectMode = _selectMode.asStateFlow()

    private val _generatedPassword = MutableStateFlow("")
    val generatedPassword = _generatedPassword.asStateFlow()

    private val _passwordStrength = MutableStateFlow("weak")
    val passwordStrength = _passwordStrength.asStateFlow()

    init {

        viewModelScope.launch {
            generatedInitialPassword()
        }

    }
    fun toggleSwitch(type: SwitchType, isChecked: Boolean) {
        val currentSwitches = _switchStates.value.toMutableMap()
        val numSwitchesOn = currentSwitches.values.count { it }
        if (isChecked && numSwitchesOn >= 3 && _sliderValue.value.toInt() == 4) return
        currentSwitches[type] = isChecked
        _switchStates.value = currentSwitches
        preferences.setSwitchState(type, isChecked)
        viewModelScope.launch { generatedPassword() }
    }

    fun isSwitchOn(type: SwitchType): Boolean = switchStates.value[type] == true

    fun setSliderValue(value: Int) {
        _sliderValue.value = value
        preferences.setSliderValue(value)
        viewModelScope.launch { generatedPassword() }
    }

    fun setSelectMode(mode: ModeSelector) {
        _selectMode.value = mode
        preferences.setMode(mode)
        viewModelScope.launch { generatedPassword() }
    }

    private suspend fun generatedInitialPassword() {
        val result = passwordGenerator.generatePassword(
            mode = _selectMode.value,
            length = _sliderValue.value.toInt(),
            switches = _switchStates.value
        )

        when (result) {
            is PasswordGenerator.PasswordResult.Success -> {
                _generatedPassword.value = result.password
                _passwordStrength.value = result.strength
            }
            is PasswordGenerator.PasswordResult.Error -> {
                _generatedPassword.value = ""
                _passwordStrength.value = "weak"
            }
        }
    }

    private suspend fun generatedPassword() {
        val result = passwordGenerator.generatePassword(
            mode = _selectMode.value,
            length = _sliderValue.value.toInt(),
            switches = _switchStates.value
        )

        when (result) {
            is PasswordGenerator.PasswordResult.Success -> {
                _generatedPassword.value = result.password
                _passwordStrength.value = result.strength
            }
            is PasswordGenerator.PasswordResult.Error -> {
                _generatedPassword.value = ""
                _passwordStrength.value = "weak"
            }
        }
    }


}