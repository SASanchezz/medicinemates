package com.test.medicinemates.medicine

import com.test.medicinemates.utilis.DateUtils


data class MedicineModel (
    val name: String,
    val startIntakeDate: String,
    val endIntakeDate: String?,
    val intakeTime: String,
    val recurringType: ERecurringType,
    val daysOfWeek: String?,
) {
    fun toMedicineDbEntity(): MedicineDbEntity {
        return MedicineDbEntity(
            name = name,
            startIntakeDate = DateUtils.getSqlDateString(startIntakeDate),
            endIntakeDate = endIntakeDate?.let { DateUtils.getSqlDateString(it) },
            intakeTime = intakeTime,
            recurringType = recurringType,
            daysOfWeek = daysOfWeek
        )
    }
}
