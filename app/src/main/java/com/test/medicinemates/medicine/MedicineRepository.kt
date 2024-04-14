package com.test.medicinemates.medicine

import com.test.medicinemates.utilis.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MedicineRepository (private val medicineDao: MedicineDao) {
    suspend fun insertNewMedicine(medicineDbEntity: MedicineDbEntity) {
        withContext(Dispatchers.IO) {
            medicineDao.insertNewMedicine(medicineDbEntity)
        }
    }

    suspend fun getAllMedicine(): List<MedicineDbEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext medicineDao.getAllMedicines()
        }
    }

    suspend fun getDayMedicine(date: String): List<MedicineDbEntity> {
        val sqlDate = DateUtils.getSqlDateString(date)
        return withContext(Dispatchers.IO) {
            val tmpValue = medicineDao.getAllMedicines()
            print(tmpValue)
            return@withContext medicineDao.getDayMedicines(sqlDate)
        }
    }

    suspend fun removeMedicineById(id: Long) {
        withContext(Dispatchers.IO) {
            medicineDao.deleteMedicineById(id)
        }
    }

}
