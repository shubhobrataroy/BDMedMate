package com.shubhobrataroy.bdmedmate.data

import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.MedDataSource
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
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

    override suspend fun getAllGenerics(
        searchQuery: String,
        byNameAsc: Boolean,
        country: Country
    ): List<Generic> {
        return countryWiseDataSources[country]?.getAllGenerics(searchQuery, byNameAsc = byNameAsc)
            ?: emptyList()
    }

    override suspend fun getAllCompanies(
        searchQuery: String,
        byNameAsc: Boolean,
        country: Country
    ): List<Company> {
        return countryWiseDataSources[country]?.getAllCompany(searchQuery, byNameAsc = byNameAsc)
            ?: emptyList()
    }
}