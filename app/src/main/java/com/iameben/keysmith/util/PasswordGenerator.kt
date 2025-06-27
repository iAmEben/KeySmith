package com.iameben.keysmith.util


import androidx.compose.material3.SnackbarHostState
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.ui.components.enums.ModeSelector
import com.iameben.keysmith.ui.components.enums.SwitchType
import java.security.SecureRandom
import kotlin.math.max
import kotlin.math.min

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
            snackBarHostState.showSnackbar("At least one switch type is needed!")
            return PasswordResult.Error("At least one switch type is needed")
        }
        if (effectiveLength < 4) {
            return PasswordResult.Error("Password length must be at least 4.")
        }
        if (effectiveLength == 4 && effectiveSwitches.values.count { it } == 4) {
            snackBarHostState.showSnackbar("Cannot use all switches if password count is four")
            return PasswordResult.Error("Cannot use all switches if password count is four")
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
        val enabledSwitches = switches.filter { it.value }.keys
        if (enabledSwitches.isEmpty()) return ""
        val charPool = buildList {
            if (switches[SwitchType.UPPERCASE] == true) addAll('A'..'Z')
            if (switches[SwitchType.LOWERCASE] == true) addAll('a'..'z')
            if (switches[SwitchType.NUMBERS] == true) addAll('0'..'9')
            if (switches[SwitchType.SPECIAL_CHARACTERS] == true) addAll("!@#$%^&*()_+-=[]{}|;:,.<>?".toList())
        }

        val requiredChars = mutableListOf<Char>()
        val uppercasePool = ('A'..'Z').toList()
        val lowercasePool = ('a'..'z').toList()
        val numberPool = ('0'..'9').toList()
        val specialPool = "!@#$%^&*()_+-=[]{}|;:,.<>?".toList()

        enabledSwitches.forEach { switch ->
            val sampleChar = when (switch) {
                SwitchType.UPPERCASE -> uppercasePool[secureRandom.nextInt(uppercasePool.size)]
                SwitchType.LOWERCASE -> lowercasePool[secureRandom.nextInt(lowercasePool.size)]
                SwitchType.NUMBERS -> numberPool[secureRandom.nextInt(numberPool.size)]
                SwitchType.SPECIAL_CHARACTERS -> specialPool[secureRandom.nextInt(specialPool.size)]
            }
            requiredChars.add(sampleChar)

        }

        val remainingLength = length - requiredChars.size
        if (remainingLength < 0) return requiredChars.joinToString("")

        val randomChars = (1..remainingLength)
            .map { charPool[secureRandom.nextInt(charPool.size)] }
            .toMutableList()

        val allChars = (requiredChars + randomChars).toMutableList()
        repeat(length) { i ->
            val j = secureRandom.nextInt(i + 1)
            allChars[i] = allChars.set(j, allChars[i])
        }
        return allChars.joinToString("")
    }

    private fun generateSmartPassword(length: Int, switches: Map<SwitchType, Boolean>): String {
        if (dictionary.isEmpty()) return ""
        val enabledSwitches = switches.filter { it.value }.keys
        val hasNumberOrSpecial = enabledSwitches.any { it == SwitchType.NUMBERS || it == SwitchType.SPECIAL_CHARACTERS }
        val maxWords = if (hasNumberOrSpecial) 3 else 4

        return when {
            length == 4 && enabledSwitches.size == 3 -> {

                val word = dictionary.filter { it.length == 2 }.random(secureRandom)
                val requiredChars = mutableListOf<Char>()
                val uppercasePool = ('A'..'Z').toList()
                val lowercasePool = ('a'..'z').toList()
                val numberPool = ('0'..'9').toList()
                val specialPool = "!@#$%^&*()_+-=[]{}|;:,.<>?".toList()

                enabledSwitches.forEach { switch ->
                    val sampleChar = when (switch) {
                        SwitchType.UPPERCASE -> uppercasePool[secureRandom.nextInt(uppercasePool.size)]
                        SwitchType.LOWERCASE -> lowercasePool[secureRandom.nextInt(lowercasePool.size)]
                        SwitchType.NUMBERS -> numberPool[secureRandom.nextInt(numberPool.size)]
                        SwitchType.SPECIAL_CHARACTERS -> specialPool[secureRandom.nextInt(specialPool.size)]
                    }.takeIf { true }
                    sampleChar?.let { requiredChars.add(it) }

                }

                val remainingLength = length - 2 - requiredChars.size
                val randomChars = if (remainingLength > 0) {
                    val charPool = buildList {
                        if (switches[SwitchType.UPPERCASE] == true) addAll('A'..'Z')
                        if (switches[SwitchType.LOWERCASE] == true) addAll('a'..'z')
                        if (switches[SwitchType.NUMBERS] == true) addAll('0'..'9')
                        if (switches[SwitchType.SPECIAL_CHARACTERS] == true) addAll("!@#$%^&*()_+-=[]{}|;:,.<>?".toList())
                    }
                    (1..remainingLength).map { charPool[secureRandom.nextInt(charPool.size)] }
                } else emptyList()
                (listOf(word) + requiredChars + randomChars).joinToString("").take(length)
            }
            else -> {

                val wordCount = min(max(1, (length / 2).coerceAtMost(maxWords)), enabledSwitches.size)
                val words = (2..wordCount).joinToString("") {
                    dictionary.filter { it.length <= length / wordCount }.random(secureRandom)
                }
                val wordLength = words.length
                val remainingLength = length - wordLength

                val requiredChars = mutableListOf<Char>()
                val uppercasePool = ('A'..'Z').toList()
                val lowercasePool = ('a'..'z').toList()
                val numberPool = ('0'..'9').toList()
                val specialPool = "!@#$%^&*()_+-=[]{}|;:,.<>?".toList()

                enabledSwitches.forEach { switch ->
                    val sampleChar = when (switch) {
                        SwitchType.UPPERCASE -> uppercasePool[secureRandom.nextInt(uppercasePool.size)]
                        SwitchType.LOWERCASE -> lowercasePool[secureRandom.nextInt(lowercasePool.size)]
                        SwitchType.NUMBERS -> numberPool[secureRandom.nextInt(numberPool.size)]
                        SwitchType.SPECIAL_CHARACTERS -> specialPool[secureRandom.nextInt(specialPool.size)]
                    }.takeIf { true }
                    sampleChar?.let { requiredChars.add(it) }

                }
                val fillLength = remainingLength - requiredChars.size
                val randomChars = if (fillLength > 0) {
                    val charPool = buildList {
                        if (switches[SwitchType.UPPERCASE] == true) addAll('A'..'Z')
                        if (switches[SwitchType.LOWERCASE] == true) addAll('a'..'z')
                        if (switches[SwitchType.NUMBERS] == true) addAll('0'..'9')
                        if (switches[SwitchType.SPECIAL_CHARACTERS] == true) addAll("!@#$%^&*()_+-=[]{}|;:,.<>?".toList())
                    }
                    (1..fillLength).map { charPool[secureRandom.nextInt(charPool.size)] }
                } else emptyList()
                (listOf(words) + requiredChars + randomChars).joinToString("").take(length)
            }
        }
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

    private fun <T> List<T>.random(random: SecureRandom): T = this[random.nextInt(size)]

}