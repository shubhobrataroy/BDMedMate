package com.shubhobrataroy.bdmedmate.domain

import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
interface MedDataSource {
    suspend fun getAllMedicines(medSearchQuery: String="", byMedNameAsc: Boolean = true): List<Medicine>
    suspend fun getAllGenerics(genericSearchQuery:String ="",byNameAsc: Boolean = true): List<Generic>
    suspend fun getAllCompany(): List<Company>
}