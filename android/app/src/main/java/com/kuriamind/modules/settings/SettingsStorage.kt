package com.kuriamind.modules.settings

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager

class SettingsStorage(context: Context) {
    private val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    fun saveValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getValue(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getAllValues(): Map<String, *> {
        Log.d("DEBUG", "Pasas por aqui")
        return sharedPreferences.all
    }
}
