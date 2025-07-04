package com.iameben.keysmith.ui.passwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iameben.keysmith.data.local.dao.PasswordDao
import com.iameben.keysmith.data.local.entity.PasswordEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PasswordViewmodel @Inject constructor(
    private val passwordDao: PasswordDao
): ViewModel() {
    private val _passwords = MutableStateFlow<List<PasswordEntity>>(emptyList())
    val passwords: StateFlow<List<PasswordEntity>> = _passwords.asStateFlow()

    init {
        viewModelScope.launch {
            passwordDao.getAllPasswords().collect { passwords ->
                _passwords.value = passwords
            }
        }
    }

    fun updatePasswordTitle(password: PasswordEntity, newTitle: String) {
        viewModelScope.launch {
            passwordDao.update(password.copy(title = newTitle))
        }
    }

    fun deletePassword(password: PasswordEntity) {
        viewModelScope.launch {
            passwordDao.delete(password)
        }
    }

}