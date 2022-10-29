package api.controllers

import java.util.regex.Pattern

class Validator {
    private val specialCharacterPattern = Pattern.compile("[¡!@#$%&*()_+=|<>¿?{}.\\[\\]~-]", Pattern.CASE_INSENSITIVE)

    fun containsNumber(toCheck: String?): Boolean {
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

    fun isValidEMail(eMail: String?): Boolean{
        //val regExp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        //return regExp.toRegex().matches(eMail as CharSequence)
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(eMail).matches()
    }

    fun isValidUserType(type: String?): Boolean {
        return type == "CLIENT" || type == "ASSISTENCE"
    }
}
