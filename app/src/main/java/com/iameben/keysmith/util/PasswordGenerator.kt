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
        val isUpperOrLowercase = enabledSwitches.any { it == SwitchType.UPPERCASE || it == SwitchType.LOWERCASE }
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

            isUpperOrLowercase -> {


                val uppercasePool = ('A'..'Z').toList()
                val lowercasePool = ('a'..'z').toList()
                val numberPool = ('0'..'9').toList()
                val specialPool = "!@#$%^&*()_+-=[]{}|;:,.<>?".toList()

                val wordCount = when {
                    length < 6 -> 1
                    length < 10 -> 2
                    else -> 3
                }.coerceAtMost(dictionary.size)


                val separatorsNeeded = maxOf(0, wordCount - 1)
                val maxTotalWordLength = length - separatorsNeeded
                val avgWordLength = if (wordCount > 0) maxTotalWordLength / wordCount else 0


                val words = mutableListOf<String>()
                var currentLength = 0
                for (i in 0 until wordCount) {
                    val remainingLength = maxTotalWordLength - currentLength
                    val maxWordLength = minOf(remainingLength, avgWordLength).coerceAtLeast(2)
                    val availableWords = dictionary.filter { it.length in 2..maxWordLength }
                    if (availableWords.isEmpty()) break
                    val word = availableWords.random(secureRandom)
                    words.add(word)
                    currentLength += word.length
                }

                if (words.isEmpty()) {
                    val charPool = buildCharPool(switches)
                    return (1..length).map { charPool.random(secureRandom) }.joinToString("")
                }


                val casedWords = words.map { word ->
                    val lowerEnabled = switches[SwitchType.LOWERCASE] == true
                    val upperEnabled = switches[SwitchType.UPPERCASE] == true
                    when {
                        lowerEnabled && upperEnabled -> {
                            when (secureRandom.nextInt(3)) {
                                0 -> word.lowercase()
                                1 -> word.uppercase()
                                else -> word.map { if (secureRandom.nextBoolean()) it.uppercaseChar() else it.lowercaseChar() }.joinToString("")
                            }
                        }
                        lowerEnabled -> word.lowercase()
                        upperEnabled -> word.uppercase()
                        else -> word
                    }
                }

                val charPool = buildCharPool(switches)
                val separators = (1..separatorsNeeded).map { charPool.random(secureRandom).toString() }


                val passwordBuilder = StringBuilder()
                for (i in casedWords.indices) {
                    passwordBuilder.append(casedWords[i])
                    if (i < separators.size) passwordBuilder.append(separators[i])
                }


                val remainingLength = length - passwordBuilder.length
                if (remainingLength > 0) {
                    val randomChars = (1..remainingLength).map { charPool.random(secureRandom) }
                    passwordBuilder.append(randomChars.joinToString(""))
                }


                var password = passwordBuilder.toString()
                val missingTypes = enabledSwitches.filter { switch ->
                    when (switch) {
                        SwitchType.LOWERCASE -> !password.any { it.isLowerCase() }
                        SwitchType.UPPERCASE -> !password.any { it.isUpperCase() }
                        SwitchType.NUMBERS -> !password.any { it.isDigit() }
                        SwitchType.SPECIAL_CHARACTERS -> !password.any { !it.isLetterOrDigit() }
                    }
                }

                if (missingTypes.isNotEmpty()) {
                    val positions = (0 until password.length).shuffled(secureRandom)
                    var posIndex = 0

                    missingTypes.forEach { switch ->
                        val sampleChar = when (switch) {
                            SwitchType.UPPERCASE -> uppercasePool[secureRandom.nextInt(uppercasePool.size)]
                            SwitchType.LOWERCASE -> lowercasePool[secureRandom.nextInt(lowercasePool.size)]
                            SwitchType.NUMBERS -> numberPool[secureRandom.nextInt(numberPool.size)]
                            SwitchType.SPECIAL_CHARACTERS -> specialPool[secureRandom.nextInt(specialPool.size)]
                        }
                        if (posIndex < positions.size) {
                            password = password.replaceRange(positions[posIndex], positions[posIndex] + 1, sampleChar.toString())
                            posIndex++
                        }

                    }
                }

                return password.take(length)
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

    private fun applyCasing(word: String, switches: Map<SwitchType, Boolean>, random: SecureRandom): String{
        val upperEnabled = switches[SwitchType.UPPERCASE] == true
        val lowerEnabled = switches[SwitchType.LOWERCASE] == true

        return when {
            upperEnabled && lowerEnabled -> {
                when (random.nextInt(3)) {
                    0 -> word.uppercase()
                    1 -> word.lowercase()
                    2 -> word.map { if (random.nextBoolean()) it.uppercaseChar() else it.lowercaseChar() }.joinToString("")
                    else -> word
                }
            }
            upperEnabled -> word.uppercase()
            lowerEnabled -> word.lowercase()
            else -> word
        }
    }


    private fun buildCharPool(switches: Map<SwitchType, Boolean>): List<Char> {
        return buildList {
            if (switches[SwitchType.LOWERCASE] == true) addAll('a'..'z')
            if (switches[SwitchType.UPPERCASE] == true) addAll('A'..'Z')
            if (switches[SwitchType.NUMBERS] == true) addAll('0'..'9')
            if (switches[SwitchType.SPECIAL_CHARACTERS] == true) "!@#$%^&*()_+-=[]{}|;:,.<>?".toList()
        }
    }

}