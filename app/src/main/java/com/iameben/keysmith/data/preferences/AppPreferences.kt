package com.iameben.keysmith.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.iameben.keysmith.ui.components.enums.SwitchType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFS_NAME = "app_prefs"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun switchState(type: SwitchType, isChecked: Boolean) {
        prefs.edit() { putBoolean(type.name, isChecked) }
    }

    fun getSwitchState(type: SwitchType): Boolean = prefs.getBoolean(type.name, false)

    fun getAllSwitchStates(): Map<SwitchType, Boolean> =
        SwitchType.entries.associateWith { getSwitchState(it) }

    fun clear() {
        prefs.edit() { clear() }
    }
}