package com.test.medicinemates.connections.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.medicinemates.medicine.MedicineDao
import com.test.medicinemates.medicine.MedicineDbEntity
import com.test.medicinemates.utilis.RecurringTypeConverter

@Database(
    version = 1,
    entities = [
        MedicineDbEntity::class,
    ]
)
@TypeConverters(RecurringTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMedicineDao(): MedicineDao
}
