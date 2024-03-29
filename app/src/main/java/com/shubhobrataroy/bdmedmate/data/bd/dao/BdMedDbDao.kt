package com.shubhobrataroy.bdmedmate.data.bd.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.shubhobrataroy.bdmedmate.data.bd.entity.*
import com.shubhobrataroy.bdmedmate.data.bd.mapper.MedicineDetailed
import kotlinx.coroutines.flow.Flow

@Dao
interface BdMedDbDao {

    @Query("select * from BRAND order by brand_name asc")
    fun getAllBrandData(
    ): List<MedicineEntity>

    @Transaction
    @Query("select * from BRAND order by brand_name asc")
    fun getAllMedicineDataDetailed(
    ): List<MedicineDetailed>


    @Transaction
    @RawQuery
    fun getAllBrandDataDynamicQuery(
        query: SupportSQLiteQuery
    ): List<MedicineDetailed>


    @RawQuery
    fun getAllMedGenericsData(query: SupportSQLiteQuery): List<MedGenericsEntity>


    @Query("select * from company_name")
    fun getAllCompanyData(): List<CompanyEntity>

    @RawQuery
    fun getAllCompanyDataByCustomQuery(query: SupportSQLiteQuery): List<CompanyEntity>


    @Query("select * from indication")
    fun getAllIndications(): List<IndicationsEntity>

    @Query("select * from generic where generic_id = :genericId")
    fun getGenericById(genericId: String): Flow<MedGenericsEntity?>


    @Query("select * from BRAND where generic_id = :genericId and form like :form and strength like :strength")
    fun getSimilarMedicine(genericId: String, form:String="%%", strength:String="%%"): Flow<List<MedicineEntity>>

    @Query("select * from BRAND where generic_id = :genericId")
    fun getMedsByGenerics(genericId: String): Flow<List<MedicineEntity>>

    @Query("select * from BRAND where company_id = :companyId")
    fun getMedicinesCompanyId(companyId: String): Flow<List<MedicineEntity>>

    @Query("select * from COMPANY_NAME where company_id = :companyId")
    fun getCompanyDetailsByCompanyId(companyId: String): Flow<CompanyEntity>

    @Query("select * from BRAND where generic_id = :genericId and brand_id != :excludeMedId")
    fun getOtherSimilarMedicines(
        genericId: String,
        excludeMedId: String = "-1"
    ): List<MedicineEntity>

    @Query("select * from indication_generic_index where generic_id = :genericId")
    fun getIndicationId(genericId: String): GenericIndicationEntity?


}