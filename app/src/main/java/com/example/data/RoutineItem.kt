package com.example.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(tableName = "routine_items")
data class RoutineItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dayOfWeek: Int, // 1 = Mon, 2 = Tue, 3 = Wed, 4 = Thu, 5 = Fri, 6 = Sat, 7 = Sun
    val courseName: String,
    val startTime: String, // e.g. "09:00 AM"
    val endTime: String, // e.g. "10:30 AM"
    val roomNumber: String? = null
) {
    @get:Ignore
    val startTimeMinutes: Int
        get() = parseTimeToMinutes(startTime)

    @get:Ignore
    val endTimeMinutes: Int
        get() = parseTimeToMinutes(endTime)

    companion object {
        fun parseTimeToMinutes(timeStr: String?): Int {
            if (timeStr.isNullOrBlank()) return 0
            return try {
                val cleaned = timeStr.trim().uppercase(Locale.US).replace(".", "")
                val isPm = cleaned.contains("PM")
                val isAm = cleaned.contains("AM")
                val timePart = cleaned.replace("AM", "").replace("PM", "").trim()
                val parts = timePart.split(":")
                if (parts.isNotEmpty()) {
                    var hour = parts[0].trim().toIntOrNull() ?: 0
                    val minute = if (parts.size > 1) parts[1].trim().toIntOrNull() ?: 0 else 0
                    if (isPm) {
                        if (hour < 12) hour += 12
                    } else if (isAm) {
                        if (hour == 12) hour = 0
                    }
                    hour * 60 + minute
                } else 0
            } catch (_: Exception) {
                0
            }
        }
    }
}
