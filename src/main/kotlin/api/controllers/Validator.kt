package api.controllers

import java.util.regex.Matcher
import java.util.regex.Pattern

class Validator {
    fun containsNumber(toCheck: String): Boolean {
        return toCheck.any { Character.isDigit(it) }
    }

    fun containsSpecialCharacter(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val regexCharacterSpecial = ("(?=.*[-+_!@#$%^&*.,?{}]).+$")
        val special: Pattern = Pattern.compile(regexCharacterSpecial)
        val mSpecial: Matcher = special.matcher(str)
        return mSpecial.matches()
    }
}
