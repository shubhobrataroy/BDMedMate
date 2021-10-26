package com.shubhobrataroy.bdmedmate.data

import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.MedDataSource
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import dagger.Lazy
import java.util.HashMap
import javax.inject.Inject

class RepositoryImpl @Inject constructor(countryWiseDataSources: Lazy<HashMap<Country, MedDataSource>>) :
    Repository {

    private val countryWiseDataSources by lazy { countryWiseDataSources.get() }

    override fun initialize() {
        countryWiseDataSources

    }

    override suspend fun getAllMedicinesByCountry(
        searchQuery: String,
        country: Country,
        byMedNameAsc: Boolean
    ) =
        countryWiseDataSources[country]?.getAllMedicines(searchQuery, byMedNameAsc) ?: emptyList()

    override suspend fun getAllGenerics(searchQuery: String, byNameAsc: Boolean, country: Country): List<MedGeneric> {
       return countryWiseDataSources[country]?.getAllGenerics(searchQuery,byNameAsc = byNameAsc)?: emptyList()
    }
}