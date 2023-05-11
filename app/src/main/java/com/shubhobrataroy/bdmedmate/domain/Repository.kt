package com.shubhobrataroy.bdmedmate.domain

import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

interface Repository {
    fun initialize()

    suspend fun getAllMedicinesByCountry(
        searchQuery: String="",
        country: Country,
        byMedNameAsc: Boolean = true,) : List<Medicine>

    suspend fun getAllGenerics(searchQuery: String="", byNameAsc: Boolean=true, country: Country=Country.Bangladesh): List<Generic>

}