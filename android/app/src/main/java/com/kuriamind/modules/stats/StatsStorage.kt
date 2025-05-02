package com.kuriamind.modules.stats

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatsStorage(context: Context) {
    private val prefs: SharedPreferences =
            context.applicationContext.getSharedPreferences("stats_storage", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val dataKey = "stats_data"
    private val listType = object : TypeToken<List<DailyStats>>() {}.type
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // YYYY-MM-DD

    companion object {
        private const val MAX_DAYS = 7
        private const val TAG = "StatsStorage"
    }

    private fun getAllStats(): MutableList<DailyStats> {
        val json = prefs.getString(dataKey, "[]") ?: "[]"
        return try {
            gson.fromJson<MutableList<DailyStats>>(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            Log.e(TAG, "Error deserializing stats data, returning empty list.", e)
            prefs.edit().remove(dataKey).apply() // Clear corrupted data
            mutableListOf()
        }
    }

    private fun saveStats(stats: List<DailyStats>) {
        val json = gson.toJson(stats)
        prefs.edit().putString(dataKey, json).apply()
    }

    private fun pruneOldStats(stats: MutableList<DailyStats>): MutableList<DailyStats> {
        if (stats.isEmpty()) return stats

        try {
            val cutoffDate = LocalDate.now().minusDays(MAX_DAYS.toLong())
            stats.removeIf {
                try {
                    LocalDate.parse(it.date, dateFormatter).isBefore(cutoffDate)
                } catch (e: Exception) {
                    Log.w(TAG, "Could not parse date '${it.date}' for pruning, removing entry.", e)
                    true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during pruning", e)
        }
        return stats
    }

    private fun findOrCreateDailyStats(
            stats: MutableList<DailyStats>,
            dateStr: String
    ): DailyStats {
        var dailyEntry = stats.find { it.date == dateStr }
        if (dailyEntry == null) {
            dailyEntry = DailyStats(date = dateStr)
            stats.add(dailyEntry)
            stats.sortByDescending { it.date }
        }
        return dailyEntry
    }

    @Synchronized
    fun incrementAppBlock(packageName: String) {
        try {
            val todayStr = LocalDate.now().format(dateFormatter)
            val allStats = getAllStats()
            val prunedStats = pruneOldStats(allStats)
            val todayStats = findOrCreateDailyStats(prunedStats, todayStr)

            val appEntry = todayStats.appStats.getOrPut(packageName) { AppDayStats() }
            appEntry.appBlockCount++
            Log.d(
                    TAG,
                    "Incremented app block for $packageName on $todayStr. New count: ${appEntry.appBlockCount}"
            )

            saveStats(prunedStats)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to increment app block for $packageName", e)
        }
    }

    @Synchronized
    fun incrementNotificationBlock(packageName: String) {
        try {
            val todayStr = LocalDate.now().format(dateFormatter)
            val allStats = getAllStats()
            val prunedStats = pruneOldStats(allStats)
            val todayStats = findOrCreateDailyStats(prunedStats, todayStr)

            val appEntry = todayStats.appStats.getOrPut(packageName) { AppDayStats() }
            appEntry.notificationBlockCount++
            Log.d(
                    TAG,
                    "Incremented notification block for $packageName on $todayStr. New count: ${appEntry.notificationBlockCount}"
            )

            saveStats(prunedStats)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to increment notification block for $packageName", e)
        }
    }

    @Synchronized
    fun getStats(startDate: LocalDate, endDate: LocalDate): List<DailyStats> {
        return try {
            val allStats = getAllStats()
            allStats
                    .filter {
                        try {
                            val statDate = LocalDate.parse(it.date, dateFormatter)
                            !statDate.isBefore(startDate) && !statDate.isAfter(endDate)
                        } catch (e: Exception) {
                            Log.w(
                                    TAG,
                                    "Could not parse date '${it.date}' for filtering, excluding entry.",
                                    e
                            )
                            false
                        }
                    }
                    .sortedByDescending { it.date }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get stats for range $startDate to $endDate", e)
            emptyList()
        }
    }
}
