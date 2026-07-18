package com.kuriamind

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KuriamindApplication : Application() {

    companion object {
        private const val PREFS_NAME = "kuriamind_prefs"
        private const val KEY_LANGUAGE = "app_language"

        fun loadAppLanguage(context: Context): String {
            return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_LANGUAGE, "en") ?: "en"
        }

        fun setAppLanguage(context: Context, lang: String) {
            context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, lang)
                .apply()
        }
    }
}
