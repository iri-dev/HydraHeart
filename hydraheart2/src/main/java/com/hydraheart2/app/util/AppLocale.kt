package com.hydraheart2.app.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object AppLocale {
    fun applyAppLanguage(code: String) {
        val locales = when (code) {
            "de" -> LocaleListCompat.forLanguageTags("de")
            "es" -> LocaleListCompat.forLanguageTags("es")
            "en_uk" -> LocaleListCompat.forLanguageTags("en-GB")
            "en_us" -> LocaleListCompat.forLanguageTags("en-US")
            else -> LocaleListCompat.getEmptyLocaleList()
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
