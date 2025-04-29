package com.kuriamind.modules.stats

data class AppDayStats(var appBlockCount: Int = 0, var notificationBlockCount: Int = 0)

data class DailyStats(
        val date: String, // Store date as ISO string "YYYY-MM-DD"
        val appStats: MutableMap<String, AppDayStats> = mutableMapOf() // Key: packageName
)
