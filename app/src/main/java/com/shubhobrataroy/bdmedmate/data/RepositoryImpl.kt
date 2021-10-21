package com.shubhobrataroy.bdmedmate.data

import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.MedDataSource
import com.shubhobrataroy.bdmedmate.domain.Repository
import dagger.Lazy
import java.util.HashMap
import javax.inject.Inject

class RepositoryImpl @Inject constructor(countryWiseDataSources: Lazy<HashMap<Country, MedDataSource>>) :
    Repository {

    private val countryWiseDataSources by lazy { countryWiseDataSources.get() }

    override fun initialize() {
        countryWiseDataSources

    }

    override suspend fun getAllMedicinesByCountry(country: Country,byMedNameAsc:Boolean) =
        countryWiseDataSources[country]?.getAllMedicines(byMedNameAsc) ?: emptyList()
}