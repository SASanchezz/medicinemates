package com.test.medicinemates.medicine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "start_intake_date")
    val startIntakeDate: String,

    @ColumnInfo(name = "end_intake_date")
    val endIntakeDate: String? = null,

    @ColumnInfo(name = "intake_time")
    val intakeTime: String,

    @ColumnInfo(name = "recurring_type")
    val recurringType: ERecurringType,

    @ColumnInfo(name = "days_of_week")
    val daysOfWeek: String? = null,
)
