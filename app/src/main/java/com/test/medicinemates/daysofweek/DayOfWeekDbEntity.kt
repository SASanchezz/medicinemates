package com.test.medicinemates.daysofweek

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days_of_week")
data class DayOfWeekDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_id")
    val medicineId: String,

    @ColumnInfo(name = "monday")
    val monday: String? = null,

    @ColumnInfo(name = "tuesday")
    val tuesday: String? = null,

    @ColumnInfo(name = "wednesday")
    val wednesday: String? = null,

    @ColumnInfo(name = "thursday")
    val thursday: String? = null,

    @ColumnInfo(name = "friday")
    val friday: String? = null,

    @ColumnInfo(name = "saturday")
    val saturday: String? = null,

    @ColumnInfo(name = "sunday")
    val sunday: String? = null,
)
