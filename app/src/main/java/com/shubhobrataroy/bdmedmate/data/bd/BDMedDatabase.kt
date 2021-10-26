package com.shubhobrataroy.bdmedmate.data.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.shubhobrataroy.bdmedmate.data.bd.dao.BdMedDbDao
import com.shubhobrataroy.bdmedmate.data.bd.entity.*
import com.shubhobrataroy.bdmedmate.data.bd.mapper.MedicineDetailed
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

    override suspend fun getAllMedicines(
        medSearchQuery: String,
        byMedNameAsc: Boolean
    ): List<Medicine> {
        val orderLogic = buildString {
            append("brand_name ")
            append(if (byMedNameAsc) "asc" else "desc")
        }

        val whereLogic = buildString {
            append("brand_name like '%$medSearchQuery%'")
            append(" COLLATE SQL_Latin1_General_CP1_CI_AS")
        }

        return dao.getAllBrandDataDynamicQuery(SimpleSQLiteQuery("select * from BRAND WHERE $whereLogic order by $orderLogic"))
            .map { it.toMedicine() }
    }

    override suspend fun getAllGenerics(genericSearchQuery:String ,byNameAsc: Boolean ): List<MedGeneric> {

        val orderLogic = buildString {
            append("generic_name ")
            append(if (byNameAsc) "asc" else "desc")
        }

        val whereLogic = buildString {
            append("generic_name like '%$genericSearchQuery%'")
        }

        val query = SimpleSQLiteQuery("select * from generic where $whereLogic order by $orderLogic")


        return dao.getAllMedGenericsData(query = query).map { it.toMedGeneric() }
    }

    override suspend fun getAllCompany(): List<Company> {
        return dao.getAllCompanyData().map { it.toCompany() }
    }

    private fun MedicineDetailed.toMedicine() = medicine.toMedicine(genericsEntity, companyEntity)

    private fun MedicineEntity.toMedicine(
        genericsEntity: MedGenericsEntity? = null,
        companyEntity: CompanyEntity? = null
    ): Medicine {
        return Medicine(brandName ?: "", form, strength,
            genericName = genericsEntity?.genericName,
            companyName = companyEntity?.companyName,
            genericFetcher = {
                genericsEntity?.toMedGeneric()
                    ?: if (genericId == null) null
                    else
                        dao.getGenericById(genericId).firstOrNull()?.toMedGeneric()
            },
            companyDetails = {
                companyEntity?.toCompany()
                    ?: if (companyId == null) null
                    else dao.getCompanyDetailsByCompanyId(companyId).firstOrNull()?.toCompany()
            },
            similarMedicines = {
                if (genericId == null) emptyList()
                else
                    dao.getMedicinesGenericId(genericId,form?:"",strength = strength ?:"")
                        .map { it.toMedicine() }
            }
        )
    }


    private fun MedGenericsEntity.toMedGeneric(): MedGeneric {
        return MedGeneric(
            genericName ?: "",
            indication,
            contraIndication = contraIndication,
            dosage = dose,
            sideEffect = sideEffect
        ) {
            dao.getMedicinesGenericId(genericId).map { it.toMedicine() }
        }
    }

    private fun CompanyEntity.toCompany() = Company(
        this.companyName ?: ""
    ) { dao.getMedicinesCompanyId(companyId).map { it.toMedicine() } }
}