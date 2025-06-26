package com.iameben.keysmith.util


import androidx.compose.material3.SnackbarHostState
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.ui.components.enums.ModeSelector
import com.iameben.keysmith.ui.components.enums.SwitchType
import java.security.SecureRandom
import kotlin.math.max

class PasswordGenerator(
    private val preferences: AppPreferences,
    private val wordListUtil: WordListUtil
) {
    private val secureRandom = SecureRandom()
    private val dictionary: List<String> by lazy { wordListUtil.loadWordList() }

    sealed class PasswordResult {
        data class Success(val password: String, val strength: String) : PasswordResult()
        data class Error(val message: String) : PasswordResult()
    }

    suspend fun generatePassword(mode: ModeSelector? = null, length: Int? = null, switches: Map<SwitchType, Boolean>? = null, snackBarHostState: SnackbarHostState): PasswordResult {
        val effectiveMode = mode ?: ModeSelector.valueOf(preferences.getString("mode_selector", "RANDOM")!!)
        val effectiveLength = length ?: preferences.getInt("slider_value", 9)
        val effectiveSwitches = switches ?: loadSwitchStatesFromPreferences()

        if (effectiveSwitches.values.none { it }){
            snackBarHostState?.showSnackbar("Please turn on at least one character type switch")
            return PasswordResult.Error("Please turn on at least one character type switch.")
        }
        if (effectiveLength < 4) {
            return PasswordResult.Error("Password length must be at least 4.")
        }
        if (effectiveMode == ModeSelector.SMART && dictionary.isEmpty()) {
            return PasswordResult.Error("Dictionary not loaded")
        }

        val password = when (effectiveMode) {
            ModeSelector.RANDOM -> generateRandomPassword(effectiveLength, effectiveSwitches)
            ModeSelector.SMART -> generateSmartPassword(effectiveLength, effectiveSwitches)
        }

        val strength = getPasswordStrength(password)
        return PasswordResult.Success(password, strength)
    }

    private fun generateRandomPassword(length: Int, switches: Map<SwitchType, Boolean>): String {
        val charPool = buildList {
            if (switches[SwitchType.UPPERCASE] == true) addAll('A'..'Z')
            if (switches[SwitchType.LOWERCASE] == true) addAll('a'..'z')
            if (switches[SwitchType.NUMBERS] == true) addAll('0'..'9')
            if (switches[SwitchType.SPECIAL_CHARACTERS] == true) addAll("!@#$%^&*()_+-=[]{}|;:,.<>?".toList())
        }
        return (4..length)
            .map { charPool[secureRandom.nextInt(charPool.size)] }
            .joinToString("")
    }

    private fun generateSmartPassword(length: Int, switches: Map<SwitchType, Boolean>): String {
        if (dictionary.isEmpty()) return ""
        val baseWord = dictionary[secureRandom.nextInt(dictionary.size)]
        val remainingLength = max(0, length - baseWord.length)
        val randomPart = generateRandomPassword(remainingLength, switches)
        return (baseWord + randomPart).take(length)
    }

    private fun getPasswordStrength(password: String): String {
        if (password.isEmpty()) return "weak"
        val hasUpper = password.any { it.isUpperCase() }
        val hasLower = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        val characterTypes = listOf(hasUpper, hasLower, hasDigit, hasSpecial).count { it }

        return when {
            password.length < 8 || characterTypes < 2 -> "weak"
            password.length >= 12 && characterTypes >= 3 -> "great"
            else -> "okay"
        }
    }

    private fun loadSwitchStatesFromPreferences(): Map<SwitchType, Boolean> {
        return SwitchType.entries.associateWith { preferences.getBool(it.name, false) }
    }
}