package com.shubhobrataroy.bdmedmate.data.bd.dao

import androidx.compose.ui.graphics.Outline
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.shubhobrataroy.bdmedmate.data.bd.entity.*

@Dao
interface BdMedDbDao {

    @Query("select * from BRAND order by brand_name asc")
    fun getAllBrandData(
    ): List<MedicineEntity>

    @RawQuery
    fun getAllBrandDataDynamicQuery(
        query : SupportSQLiteQuery
    ): List<MedicineEntity>



    @Query("select * from generic")
    fun getAllMedGenericsData(): List<MedGenericsEntity>


    @Query("select * from company_name")
    fun getAllCompanyData(): List<CompanyEntity>

    @Query("select * from indication")
    fun getAllIndications(): List<IndicationsEntity>

    @Query("select * from generic where generic_id = :genericId")
    fun getGenericById(genericId: String): List<MedGenericsEntity>


    @Query("select * from BRAND where generic_id = :genericId")
    fun getMedicinesGenericId(genericId: String): List<MedicineEntity>


    @Query("select * from BRAND where company_id = :companyId")
    fun getMedicinesCompanyId(companyId: String): List<MedicineEntity>

    @Query("select * from COMPANY_NAME where company_id = :companyId")
    fun getCompanyDetailsByCompanyId(companyId: String): List<CompanyEntity>

    @Query("select * from BRAND where generic_id = :genericId and brand_id != :excludeMedId")
    fun getOtherSimilarMedicines(genericId: String, excludeMedId: String = "-1"): List<MedicineEntity>
}