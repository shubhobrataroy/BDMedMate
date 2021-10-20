package com.shubhobrataroy.bdmedmate.domain

import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
interface MedDataSource {
    suspend fun getAllMedicines(): List<Medicine>
    suspend fun getAllGenerics(): List<MedGeneric>
    suspend fun getAllCompany(): List<Company>
}