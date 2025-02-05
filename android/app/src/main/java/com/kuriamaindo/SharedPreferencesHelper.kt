package com.kuriamaindo

import android.content.Context
import android.content.SharedPreferences
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.Arguments

class SharedPreferencesHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("BlockedAppsPrefs", Context.MODE_PRIVATE)

    fun getBlockedApps(): WritableArray {
        val blockedApps = prefs.getStringSet("blockedApps", emptySet()) ?: emptySet()
        val blockedAppsArray = Arguments.createArray()
        for (app in blockedApps) {
            blockedAppsArray.pushString(app)
        }
        return blockedAppsArray
    }

    fun saveBlockedApps(apps: List<String>) {
        val editor = prefs.edit()
        editor.putStringSet("blockedApps", apps.toSet())
        editor.apply()
    }
}
