package com.shubhobrataroy.bdmedmate.domain

import com.shubhobrataroy.bdmedmate.domain.model.Medicine

interface Repository {
    fun initialize()

    suspend fun getAllMedicinesByCountry(country: Country,byMedNameAsc:Boolean = true,) : List<Medicine>
}