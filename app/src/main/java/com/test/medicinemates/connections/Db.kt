package com.test.medicinemates.connections

import android.content.Context
import androidx.room.Room
import com.test.medicinemates.connections.core.AppDatabase
import com.test.medicinemates.medicine.MedicineRepository

object Db {
    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    public val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app.db")
            .allowMainThreadQueries()
            .build()
    }

    val medicineRepository: MedicineRepository by lazy { MedicineRepository(appDatabase.getMedicineDao()) }
}
