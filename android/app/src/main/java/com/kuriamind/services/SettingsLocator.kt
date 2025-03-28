package com.kuriamind.services

import android.content.Context
import com.kuriamind.modules.settings.SettingsStorage

object SettingsLocator {
    private var settingsStorage: SettingsStorage? = null

    fun provideSettingsStorage(context: Context): SettingsStorage {
        if (settingsStorage == null) {
            settingsStorage = SettingsStorage(context.applicationContext)
        }
        return settingsStorage!!
    }

    fun reset() {
        settingsStorage = null
    }
}
