package com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler

import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompanyListHandler constructor(
    repository: Repository,
    country: Country = Country.Bangladesh,
    isAscOrder: Boolean = true
) : ShowableListHandler(repository, country, "Medicine", isAscOrder) {

    override suspend fun getAllShowableLists(): ShowableListData {
        return ShowableListData.CompanyShowableListData(
            repository.getAllCompanies(
                country = country,
                searchQuery = "",
                byNameAsc = isAscOrder
            )
        )
    }

    override suspend fun searchItemFromRepo(searchQuery: String): ShowableListData {
        val companies = repository.getAllCompanies(searchQuery, isAscOrder, Country.Bangladesh)
        lastSuccessfulSearchQuery = searchQuery
        return ShowableListData.CompanyShowableListData(companies)
    }

    override suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData = withContext(Dispatchers.Default) {
        var filteredList = showableListData
        if (showableListData is ShowableListData.CompanyShowableListData) {
            filteredList =
                ShowableListData.CompanyShowableListData(showableListData.list.filter {
                    it.name.contains(searchQuery)
                })
        }
        lastSuccessfulSearchQuery = searchQuery
        filteredList
    }


}