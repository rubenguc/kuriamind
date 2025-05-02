package com.kuriamind.services

import android.content.Context
import com.kuriamind.modules.stats.StatsStorage

object StatsLocator {
    private var statsStorage: StatsStorage? = null
    private val lock = Any()

    fun provideStatsStorage(context: Context): StatsStorage {
        synchronized(lock) {
            if (statsStorage == null) {
                statsStorage = StatsStorage(context.applicationContext)
            }
            return statsStorage!!
        }
    }

    fun reset() {
        synchronized(lock) { statsStorage = null }
    }
}
