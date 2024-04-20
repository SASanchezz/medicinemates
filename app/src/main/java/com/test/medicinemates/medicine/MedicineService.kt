package com.test.medicinemates.medicine

import com.test.medicinemates.connections.Db


class MedicineService {
    suspend fun getDayMedicine(date: String): List<MedicineModel> {
        var medicineEntities = Db.medicineRepository.getDayMedicine(date)
        medicineEntities = medicineEntities.sortedWith(compareBy({ it.intakeTime }, { it.name }))
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
