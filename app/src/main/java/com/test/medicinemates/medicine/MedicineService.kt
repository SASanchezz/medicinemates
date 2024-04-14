package com.test.medicinemates.medicine

import com.test.medicinemates.connections.Db


class MedicineService {
    suspend fun getDayMedicine(date: String): List<MedicineModel> {
        val medicineEntities = Db.medicineRepository.getDayMedicine(date)
        return medicineEntities.map {
            MedicineModel(
                name = it.name,
                startIntakeDate = it.startIntakeDate,
                endIntakeDate = it.endIntakeDate ?: null,
                intakeTime = it.intakeTime,
                recurringType = it.recurringType,
                daysOfWeek = it.daysOfWeek
            )
        }
    }
}
