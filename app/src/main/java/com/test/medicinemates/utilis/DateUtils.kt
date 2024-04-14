package com.test.medicinemates.utilis

import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {
        fun getCurrentDate(): String {
            return java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(Date())
        }

        fun addTrailingZero(number: Int): String {
            return if (number < 10) "0$number" else number.toString()
        }

        fun getDateString(day: Int, month: Int, year: Int): String {
            return "${addTrailingZero(day)}-${addTrailingZero(month)}-$year"
        }

        fun getSqlDateString(initialDate: String): String {
            val date = getDateFromDateString(initialDate)
            return java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(date)
        }

        fun getDateFromDateString(dateString: String): Date {
            return java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .parse(dateString)!!
        }

        fun getTimeString(hour: Int, minute: Int): String {
            return "${addTrailingZero(hour)}:${addTrailingZero(minute)}"
        }
    }
}