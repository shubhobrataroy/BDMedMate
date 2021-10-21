package com.shubhobrataroy.bdmedmate.data.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.shubhobrataroy.bdmedmate.data.bd.dao.BdMedDbDao
import com.shubhobrataroy.bdmedmate.data.bd.entity.*
import com.shubhobrataroy.bdmedmate.domain.MedDataSource
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

@Database(
    entities = [MedicineEntity::class, MedGenericsEntity::class, CompanyEntity::class,
        IndicationsEntity::class, GenericIndicationEntity::class],
    version = 1
)
abstract class BDMedDatabase : RoomDatabase(), MedDataSource {

    companion object {
        fun createInstance(context: Context) =
            Room.databaseBuilder(context, BDMedDatabase::class.java, "bdDb")
                .createFromAsset("medicine.db")
                .build()

    }

    abstract fun getBDMedDao(): BdMedDbDao

    private val dao by lazy { getBDMedDao() }

    override suspend fun getAllMedicines(byMedNameAsc: Boolean): List<Medicine> {
        val orderLogic = buildString {
            append("brand_name ")
            append(if (byMedNameAsc) "asc" else "desc")
        }

        return dao.getAllBrandDataDynamicQuery(SimpleSQLiteQuery("select * from BRAND order by $orderLogic"))
            .map { it.toMedicine() }
    }

    override suspend fun getAllGenerics(): List<MedGeneric> {
        return dao.getAllMedGenericsData().map { it.toMedGeneric() }
    }

    override suspend fun getAllCompany(): List<Company> {
        return dao.getAllCompanyData().map { it.toCompany() }
    }

    private fun MedicineEntity.toMedicine(): Medicine {
        return Medicine(brandName ?: "", form, strength,
            genericFetcher = {
                if (genericId == null) null
                else
                    dao.getGenericById(genericId).firstOrNull()?.toMedGeneric()
            },
            companyDetails = {
                if (companyId == null) null
                else dao.getCompanyDetailsByCompanyId(companyId).firstOrNull()?.toCompany()
            },
            similarMedicines = {
                if (genericId == null) emptyList()
                else
                    dao.getMedicinesGenericId(genericId)
                        .map { it.toMedicine() }
            }
        )
    }

    private fun MedGenericsEntity.toMedGeneric(): MedGeneric {
        return MedGeneric(genericName ?: "") {
            dao.getMedicinesGenericId(genericId).map { it.toMedicine() }
        }
    }

    private fun CompanyEntity.toCompany() = Company(
        this.companyName ?: ""
    ) { dao.getMedicinesCompanyId(companyId).map { it.toMedicine() } }
}