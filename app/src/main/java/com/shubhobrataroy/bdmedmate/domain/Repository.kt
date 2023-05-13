package com.shubhobrataroy.bdmedmate.domain

import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

interface Repository {
    fun initialize()

    suspend fun getAllMedicinesByCountry(
        searchQuery: String = "",
        country: Country,
        byMedNameAsc: Boolean = true,
    ): List<Medicine>

    suspend fun getAllGenerics(
        searchQuery: String = "",
        byNameAsc: Boolean = true,
        country: Country = Country.Bangladesh
    ): List<Generic>

    suspend fun getAllCompanies(
        searchQuery: String = "",
        byNameAsc: Boolean = true,
        country: Country = Country.Bangladesh
    ): List<Company>
}