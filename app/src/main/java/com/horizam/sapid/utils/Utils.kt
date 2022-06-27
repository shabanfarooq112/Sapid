package com.horizam.sapid.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class Utils {

    companion object {
        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }
    }
}