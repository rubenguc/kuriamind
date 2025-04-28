package com.kuriamind.utils

import android.util.Log
import com.kuriamind.modules.blocks.Block // Import Block data class
import java.time.LocalTime

object BlockUtils {

    private const val TAG = "BlockUtils" // Tag for logging

    /** Checks if a given package name exists in a list of strings. */
    fun isPackageNameInList(packageName: String, list: List<String>): Boolean {
        return list.contains(packageName)
    }

    /**
     * Checks if the current time falls within the start and end time defined in a Block. Handles
     * overnight scenarios.
     *
     * @param block The block containing startTime and endTime.
     * @param currentTimeMinutes The current time expressed in minutes since midnight.
     * @return True if the current time is within the block's window, false otherwise.
     */
    fun isTimeWithinWindow(block: Block, currentTimeMinutes: Long): Boolean {
        // If no start or end time is set, assume it's always within the window for this check
        if (block.startTime.isNullOrEmpty() || block.endTime.isNullOrEmpty()) {
            Log.d(TAG, "Block '${block.name}' has no time limits, considered within window.")
            return true
        }

        return try {
            val blockStartTime = parseTimeStringToMinutes(block.startTime)
            val blockEndTime = parseTimeStringToMinutes(block.endTime)

            val isOvernight = blockStartTime > blockEndTime

            val result =
                    if (isOvernight) {
                        // Overnight case: Current time must be after start OR before end.
                        currentTimeMinutes >= blockStartTime || currentTimeMinutes < blockEndTime
                    } else {
                        // Same day case: Current time must be between start (inclusive) and end
                        // (exclusive).
                        currentTimeMinutes >= blockStartTime && currentTimeMinutes < blockEndTime
                    }

            if (!result && Log.isLoggable(TAG, Log.DEBUG)
            ) { // Log only if not within window for debugging
                Log.d(
                        TAG,
                        "Time check failed for Block '${block.name}': Start=$blockStartTime, End=$blockEndTime, Current=$currentTimeMinutes, Overnight=$isOvernight"
                )
            }

            result
        } catch (e: IllegalArgumentException) {
            Log.e(
                    TAG,
                    "Error parsing time for Block '${block.name}': Start='${block.startTime}', End='${block.endTime}'. Assuming not within window.",
                    e
            )
            false // Treat parse errors as not within the window
        }
    }

    /** Gets the current time as total minutes since midnight. */
    fun getCurrentTimeInMinutes(): Long {
        val now = LocalTime.now()
        return (now.hour * 60 + now.minute).toLong()
    }

    /**
     * Parses a time string (HH:mm or H:mm) into total minutes since midnight.
     *
     * @param timeString The time string to parse.
     * @return Total minutes since midnight.
     * @throws IllegalArgumentException if the format is invalid or values are out of range.
     */
    @Throws(IllegalArgumentException::class) // Declare that this can throw an exception
    fun parseTimeStringToMinutes(timeString: String): Long {
        // More robust check for format
        if (!timeString.matches(Regex("^\\d{1,2}:\\d{2}$"))) {
            val errorMsg = "Invalid time format: '$timeString'. Expected HH:mm or H:mm"
            Log.w(TAG, errorMsg)
            throw IllegalArgumentException(errorMsg)
        }
        try {
            val parts = timeString.split(":")
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()

            // Validate ranges more strictly
            if (hours !in 0..23 || minutes !in 0..59) {
                val errorMsg =
                        "Invalid time values: '$timeString'. Hours must be 0-23, minutes 0-59."
                Log.w(TAG, errorMsg)
                throw IllegalArgumentException(errorMsg)
            }
            return hours * 60L + minutes
        } catch (e: NumberFormatException) {
            val errorMsg = "Failed to parse numbers in time string: '$timeString'"
            Log.w(TAG, errorMsg, e)
            throw IllegalArgumentException(errorMsg, e)
        }
    }

    /**
     * Determines if a specific block should restrict a given package name *at this moment*. Assumes
     * the block is already considered 'active' and relevant for the *type* of blocking (app vs
     * notification) by the caller.
     *
     * @param block The Block rule to check.
     * @param packageName The package name of the app/notification being checked.
     * @return True if the package is in the block's list AND the current time is within the block's
     * window.
     */
    fun shouldBlock(block: Block, packageName: String): Boolean {
        // 1. Check if the package name matches
        if (!isPackageNameInList(packageName, block.blockedApps)) {
            return false // Package not in the list for this block
        }

        // 2. Check if the current time is within the block's defined window
        //    (Uses getCurrentTimeInMinutes internally)
        if (!isTimeWithinWindow(block, getCurrentTimeInMinutes())) {
            // Logging for time failure is handled within isTimeWithinWindow
            return false // Outside the time window for this block
        }

        // If both package and time match, blocking is required by this rule.
        Log.d(TAG, "ShouldBlock YES for package '$packageName' by block '${block.name}'.")
        return true
    }
}
