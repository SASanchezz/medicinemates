package com.test.medicinemates.utilis

import androidx.room.TypeConverter
import com.test.medicinemates.medicine.ERecurringType


class RecurringTypeConverter {
    @TypeConverter
    fun toRecurringType(value: Int) = enumValues<ERecurringType>()[value]

    @TypeConverter
    fun fromRecurringType(value: ERecurringType) = value.ordinal
}
