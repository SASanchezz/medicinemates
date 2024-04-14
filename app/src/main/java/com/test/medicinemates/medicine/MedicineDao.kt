package com.test.medicinemates.medicine

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.test.medicinemates.utilis.RecurringTypeConverter

@Dao
interface MedicineDao {
    @TypeConverters(RecurringTypeConverter::class)
    @Insert(entity = MedicineDbEntity::class)
    fun insertNewMedicine(medicine: MedicineDbEntity)

    @Query("SELECT * FROM medicines;")
    fun getAllMedicines(): List<MedicineDbEntity>

    @Query("SELECT * " +
"            FROM medicines " +
"            WHERE (" +
"                   julianday(:date) - julianday(start_intake_date) >= 0 " +
"                   AND (" +
"                      end_intake_date IS NULL " +
"                      OR julianday(:date) - julianday(end_intake_date) <= 0" +
"                   ) " +
                    //-- Over daily recurrence on the specific day of the week
"                   AND (" +
"                       (" +
"                           recurring_type = 0 " +
"                           AND (julianday(:date) - julianday(start_intake_date)) % 2 = 0 " +
"                       ) " +
                //-- Recurrence on the specific days of the week
"                       OR ( " +
"                           recurring_type = 1 " + //ON WEEKEND TYPE
"                           AND days_of_week LIKE '%' || strftime('%w', :date) || '%' " +
"                       )" +
"                   )" +
"            ); "
    )
    fun getDayMedicines(date: String): List<MedicineDbEntity>

    @Query("DELETE FROM medicines WHERE id = :medicineId")
    fun deleteMedicineById(medicineId: Long)
}