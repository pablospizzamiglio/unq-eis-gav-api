package api.controllers

import java.util.regex.Matcher
import java.util.regex.Pattern

class Validator {
    fun containsNumber(toCheck: String): Boolean {
        var bool = false
        for (element in toCheck) {
            if (Character.isDigit(element)) {
                bool = true
            }
        }
        return bool
    }

    fun containsSpecialCharacter(str: String?): Boolean {
        val regexCaracterSpecial = ("(?=.*[-+_!@#$%^&*.,?{}]).+$")
        val special: Pattern = Pattern.compile(regexCaracterSpecial)
        if (str == null) {
            println("No")
            return false
        }
        val mSpecial: Matcher = special.matcher(str)
        return mSpecial.matches()
    }
}