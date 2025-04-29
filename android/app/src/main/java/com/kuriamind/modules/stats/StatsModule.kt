package com.kuriamind.modules.stats

import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.gson.Gson
import com.kuriamind.services.StatsLocator
import java.time.LocalDate

class StatsModule(reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    private val statsStorage = StatsLocator.provideStatsStorage(reactContext)
    private val gson = Gson()

    override fun getName(): String {
        return "StatsModule"
    }

    @ReactMethod
    fun queryStats(filter: String, promise: Promise) {
        try {
            val today = LocalDate.now()
            val startDate: LocalDate
            val endDate: LocalDate = today

            when (filter) {
                "today" -> {
                    startDate = today
                }
                "last3days" -> {
                    startDate = today.minusDays(2) // Today + 2 previous days
                }
                "last7days" -> {
                    startDate = today.minusDays(6) // Today + 6 previous days
                }
                else -> {
                    promise.reject(
                            "INVALID_FILTER",
                            "Invalid filter specified: $filter. Use 'today', 'last3days', or 'last7days'."
                    )
                    return
                }
            }

            Log.d(name, "Querying stats from $startDate to $endDate (Filter: $filter)")
            val results = statsStorage.getStats(startDate, endDate)

            val jsonResult = gson.toJson(results)
            promise.resolve(jsonResult)
        } catch (e: Exception) {
            Log.e(name, "Error querying stats with filter '$filter'", e)
            promise.reject("QUERY_ERROR", "Failed to query stats data: ${e.message}", e)
        }
    }
}
