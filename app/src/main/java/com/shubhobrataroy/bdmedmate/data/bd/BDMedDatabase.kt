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
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import kotlinx.coroutines.flow.map

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

    override suspend fun getAllGenerics(
        genericSearchQuery: String,
        byNameAsc: Boolean
    ): List<Generic> {

        val orderLogic = buildString {
            append("generic_name ")
            append(if (byNameAsc) "asc" else "desc")
        }

        val whereLogic = buildString {
            append("generic_name like '%$genericSearchQuery%'")
        }

        val query =
            SimpleSQLiteQuery("select * from generic where $whereLogic order by $orderLogic")


        return dao.getAllMedGenericsData(query = query).map { it.toMedGeneric() }
    }

    override suspend fun getAllCompany(searchQuery: String, byNameAsc: Boolean): List<Company> {
        val orderLogic = buildString {
            append("company_name ")
            append(if (byNameAsc) "asc" else "desc")
        }

        val whereLogic = buildString {
            append("company_name like '%$searchQuery%'")
        }

        val query =
            SimpleSQLiteQuery("select * from company_name where $whereLogic order by $orderLogic")



        return dao.getAllCompanyDataByCustomQuery(query).map { it.toCompany() }
    }

    private fun MedicineDetailed.toMedicine() = medicine.toMedicine(genericsEntity, companyEntity)

    private fun MedicineEntity.toMedicine(
        genericsEntity: MedGenericsEntity? = null,
        companyEntity: CompanyEntity? = null
    ): Medicine {
        return Medicine(brandName ?: "", form, strength,
            genericName = genericsEntity?.genericName,
            companyName = companyEntity?.companyName,
            generic = genericId?.let { genericId ->
                dao.getGenericById(genericId).map {
                    it?.toMedGeneric()
                }
            },
            companyDetails = companyId?.let { companyId ->
                dao.getCompanyDetailsByCompanyId(companyId).map { it.toCompany() }
            },
            similarMedicines = genericId?.run {
                dao.getSimilarMedicine(
                    this,
                    form ?: "%%",
                    strength = strength ?: "%%"
                ).map { list -> list.map { medEntity -> medEntity.toMedicine() } }
            }
        )
    }


    private fun MedGenericsEntity.toMedGeneric(): Generic {
        return Generic(
            genericName ?: "",
            indication,
            contraIndication = contraIndication,
            dosage = dose,
            sideEffect = sideEffect,
            medicines = dao.getMedsByGenerics(genericId)
                .map { list -> list.map { medEntity -> medEntity.toMedicine() } }
        )
    }

    private fun CompanyEntity.toCompany() = Company(
        this.companyName ?: "",
        dao.getMedicinesCompanyId(companyId)
            .map { it.map { medicineEntity -> medicineEntity.toMedicine() } }
    )
}