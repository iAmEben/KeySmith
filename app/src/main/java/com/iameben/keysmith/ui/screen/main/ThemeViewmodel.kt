package com.iameben.keysmith.ui.screen.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewmodel @Inject constructor() : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun initializeTheme(isSystemInDarkTheme: Boolean) {
        _isDarkTheme.value = isSystemInDarkTheme
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}