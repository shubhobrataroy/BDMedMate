package com.shubhobrataroy.bdmedmate.data.bdDb.dao

import androidx.room.Dao
import androidx.room.Query
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.CompanyEntity
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.IndicationsEntity
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.MedicineEntity
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.MedGenericsEntity

@Dao
interface BdMedDbDao {

    @Query("select * from BRAND")
    fun getAllBrandData(): List<MedicineEntity>

    @Query("select * from generic")
    fun getAllMedGenericsData(): List<MedGenericsEntity>


    @Query("select * from company_name")
    fun getAllCompanyData(): List<CompanyEntity>

    @Query("select * from indication")
    fun getAllIndications(): List<IndicationsEntity>


}