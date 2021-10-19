package com.shubhobrataroy.bdmedmate.data.bdDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shubhobrataroy.bdmedmate.data.bdDb.dao.BdMedDbDao
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.*

@Database(
    entities = [MedicineEntity::class, MedGenericsEntity::class, CompanyEntity::class,
        IndicationsEntity::class, GenericIndicationEntity::class],
    version = 1
)
abstract class BDMedDatabase : RoomDatabase() {
    abstract fun getBDMedDao(): BdMedDbDao
}