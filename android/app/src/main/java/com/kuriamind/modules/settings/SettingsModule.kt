package com.kuriamind.modules.settings

import android.util.Log
import com.facebook.react.BuildConfig
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.gson.Gson

class SettingsModule(reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    private val storage = SettingsStorage(reactContext)

    override fun getName(): String {
        return "SettingsModule"
    }

    @ReactMethod
    fun getSettings(promise: Promise) {
        try {
            val settings = storage.getAllValues()

            val json = Gson().toJson(settings)

            promise.resolve(json)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error getAllBlocks: $e")
            }
            promise.reject("ERROR_GET_SETTINGS", "Failed to get block data", e)
        }
    }

    @ReactMethod
    fun setSetting(key: String, value: String, promise: Promise) {
        try {
            storage.saveValue(key, value)
            promise.resolve(null)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error setAllBlocks: $e")
            }
            promise.reject("ERROR_SET_SETTINGS", "Failed to set block data", e)
        }
    }
}
