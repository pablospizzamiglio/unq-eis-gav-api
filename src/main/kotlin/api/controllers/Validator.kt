package api.controllers

import java.util.regex.Pattern

class Validator {
    private val specialCharacterPattern = Pattern.compile("[!@#$%&*()_+=|<>?{}.\\[\\]~-]", Pattern.CASE_INSENSITIVE)

    fun containsNumbers(toCheck: String?): Boolean {
        if (toCheck == null) {
            return false
        }
        return toCheck.any { Character.isDigit(it) }
    }

    fun isAllNumbers(toCheck: String?): Boolean {
        if (toCheck == null) {
            return false
        }
        return toCheck.all { Character.isDigit(it) }
    }

    fun containsSpecialCharacter(toCheck: String?): Boolean {
        if (toCheck == null) {
            return false
        }
        val matcher = specialCharacterPattern.matcher(toCheck)
        return matcher.find()
    }
}
