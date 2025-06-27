package com.iameben.keysmith.ui.screen.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.material3.SnackbarHostState
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

    private val _snackBarHostState = MutableStateFlow(SnackbarHostState())
    val snackBarHostState = _snackBarHostState.asStateFlow()

    init {

        viewModelScope.launch {
            generatedInitialPassword(_snackBarHostState.value)
        }

    }


    fun toggleSwitch(type: SwitchType, isChecked: Boolean) {
        val currentSwitches = _switchStates.value.toMutableMap()
        val numSwitchesOn = currentSwitches.values.count { it }

        if (!isChecked && numSwitchesOn == 1 && currentSwitches[type] == true){
            viewModelScope.launch{
                _snackBarHostState.value.showSnackbar(
                    message = "At least one switch must be enabled"
                )
            }
            return
        }
        if (isChecked && numSwitchesOn >= 3 && _sliderValue.value.toInt() == 4) {
            viewModelScope.launch{
                _snackBarHostState.value.showSnackbar(
                    message = "Please Increase password count"
                )
            }
            return
        }

        currentSwitches[type] = isChecked
        _switchStates.value = currentSwitches
        preferences.setSwitchState(type, isChecked)
        viewModelScope.launch { generatedPassword(_snackBarHostState.value) }
    }

    fun isSwitchOn(type: SwitchType): Boolean = switchStates.value[type] == true

    fun setSliderValue(value: Int) {
        val length = _sliderValue.value.toInt()
        val currentSwitches = _switchStates.value.toMutableMap()
        val numSwitchesOn = currentSwitches.values.count { it }
        _sliderValue.value = value
        preferences.setSliderValue(value)

        if (length == 4 && numSwitchesOn == 4) {
            currentSwitches[SwitchType.UPPERCASE] = false
            _switchStates.value = currentSwitches
            preferences.setSwitchState(SwitchType.UPPERCASE, false)
            viewModelScope.launch {
                _snackBarHostState.value.showSnackbar("Cannot use all switches if word count is four")
            }
            return
        }
        viewModelScope.launch { generatedPassword(_snackBarHostState.value) }
    }

    fun setSelectMode(mode: ModeSelector) {
        _selectMode.value = mode
        preferences.setMode(mode)
//        viewModelScope.launch { generatedPassword(_snackBarHostState.value) }
    }

    fun setSnackBarHotState(snackBarHostState: SnackbarHostState) {
        _snackBarHostState.value = snackBarHostState
    }

    private suspend fun generatedInitialPassword(snackBarHostState: SnackbarHostState) {
        val result = passwordGenerator.generatePassword(
            mode = _selectMode.value,
            length = _sliderValue.value.toInt(),
            switches = _switchStates.value,
            snackBarHostState = snackBarHostState
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

    suspend fun generatedPassword(snackBarHostState: SnackbarHostState) {
        val result = passwordGenerator.generatePassword(
            mode = _selectMode.value,
            length = _sliderValue.value.toInt(),
            switches = _switchStates.value,
            snackBarHostState = snackBarHostState
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

    fun copyToClipboard(context: Context, label: String = "text", text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        viewModelScope.launch {
            _snackBarHostState.value.showSnackbar("Password Copied!")
        }
    }


}