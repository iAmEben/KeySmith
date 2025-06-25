package com.iameben.keysmith.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.iameben.keysmith.ui.components.enums.SwitchType
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.iameben.keysmith.ui.components.enums.ModeSelector

@Singleton
class AppPreferences @Inject constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_SLIDER_VALUE = "slider_value"
        private const val KEY_MODE_SELECTOR = "mode_selector"
        private const val DEFAULT_MODE_SELECTOR = "RANDOM"
        private const val DEFAULT_SLIDER_VALUE = 9
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        ?: throw IllegalStateException("Context is null")

    fun setSwitchState(type: SwitchType, isChecked: Boolean) {
        prefs.edit() { putBoolean(type.name, isChecked) }
    }

    fun getSwitchState(type: SwitchType): Boolean = prefs.getBoolean(type.name, false)

    fun getAllSwitchStates(): Map<SwitchType, Boolean> =
        SwitchType.entries.associateWith { getSwitchState(it) }

    fun setSliderValue(value: Int) {
        prefs.edit() { putInt(KEY_SLIDER_VALUE, value) }
    }

    fun getSliderValue(): Int = prefs.getInt(KEY_SLIDER_VALUE, DEFAULT_SLIDER_VALUE)

    fun setMode(mode: ModeSelector) {
        prefs.edit() { putString(KEY_MODE_SELECTOR, mode.name) }
    }

    fun getMode(): ModeSelector {
        val modeName = prefs.getString(KEY_MODE_SELECTOR, DEFAULT_MODE_SELECTOR)
        return try {
            ModeSelector.valueOf(modeName ?: DEFAULT_MODE_SELECTOR)
        } catch (e: IllegalArgumentException) {
            ModeSelector.RANDOM
        }
    }

    fun setString(key: String, value: String) = prefs.edit() { putString(key, value) }

    fun getString(key: String, defaultValue: String): String? = prefs.getString(key, defaultValue)

    fun setInt(key: String, value: Int) = prefs.edit() { putInt(key, value) }

    fun getInt(key: String, defaultValue: Int): Int = prefs.getInt(key, defaultValue)

    fun setBool(key: String, value: Boolean) = prefs.edit() { putBoolean(key, value) }

    fun getBool(key: String, defaultValue: Boolean): Boolean? = prefs.getBoolean(key, defaultValue)

    fun clear() {
        prefs.edit() { clear() }
    }

    val context: Context
        get() = this.context

}