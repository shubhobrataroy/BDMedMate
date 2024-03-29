package com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler

import android.util.Log
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData

class GenericListHandler(
    repository: Repository,
    country: Country = Country.Bangladesh,
    isAscOrder: Boolean = true
) : ShowableListHandler(repository, country, "Generic", isAscOrder) {


    override suspend fun getAllShowableLists(): ShowableListData {
        return ShowableListData.MedicineGenericShowableListData(
            repository.getAllGenerics(
                country = country,
                searchQuery = "",
                byNameAsc = isAscOrder
            )
        )
    }

    override suspend fun searchItemFromRepo(searchQuery: String): ShowableListData {
        Log.d("GenericSearch", "Repo Search")
        val optionDataByPresets = repository.getAllGenerics(
            country = country,
            searchQuery = searchQuery,
            byNameAsc = isAscOrder
        )
        lastSuccessfulSearchQuery = searchQuery
        return ShowableListData.MedicineGenericShowableListData(optionDataByPresets)
    }

    override suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData {
        Log.d("MEDLOG", "Generic Local Search")
        val optionDataByPresets = getAllShowableLists()
        lastSuccessfulSearchQuery = searchQuery
        return optionDataByPresets
    }


}