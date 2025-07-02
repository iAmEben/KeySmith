package com.iameben.keysmith.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.iameben.keysmith.data.model.SettingsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(): ViewModel() {
    private val _items = MutableStateFlow<List<SettingsItem>>(
        listOf(
            SettingsItem("Your Passwords"),
            SettingsItem("About")
        )
    )

    val items: StateFlow<List<SettingsItem>> = _items

}