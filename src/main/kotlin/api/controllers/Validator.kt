package api.controllers

import java.util.regex.Pattern

class Validator {
    fun containsNumber(toCheck: String): Boolean {
        return toCheck.any { Character.isDigit(it) }
    }

    fun containsSpecialCharacter(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val pattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(str)
        return matcher.find()
    }
}
