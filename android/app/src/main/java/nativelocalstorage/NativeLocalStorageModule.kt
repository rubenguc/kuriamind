package com.nativelocalstorage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.google.gson.Gson

class NativeLocalStorageModule(reactContext: ReactApplicationContext) :
        NativeLocalStorageSpec(reactContext) {

    private val sharedPref: SharedPreferences =
            reactApplicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getName() = NAME

    override fun setItem(key: String, value: String) {
        try {
            sharedPref.edit().putString(key, value).apply()
        } catch (e: Exception) {
            Log.e("NativeLocalStorage", "setItem ERROR for key: '$key'", e)
        }
    }

    override fun getItem(key: String): String? {
        try {
            val value = sharedPref.getString(key, null)
            return value.toString()
        } catch (e: Exception) {
            Log.e("NativeLocalStorage", "getItem ERROR for key: '$key'", e)
            return null
        }
    }

    override fun getAll(): String {
        try {
            return Gson().toJson(sharedPref.all)
        } catch (e: Exception) {
            Log.e("NativeLocalStorage", "getAll ERROR", e)
            return ""
        }
    }

    override fun removeItem(key: String) {
        try {
            sharedPref.edit().remove(key).apply()
        } catch (e: Exception) {
            Log.e("NativeLocalStorage", "removeItem ERROR for key: '$key'", e)
        }
    }

    override fun clear() {
        try {
            sharedPref.edit().clear().apply()
        } catch (e: Exception) {
            Log.e("NativeLocalStorage", "clear ERROR", e)
        }
    }

    companion object {
        const val NAME = "NativeLocalStorage"
        private const val PREFS_NAME = "settings"
    }
}
