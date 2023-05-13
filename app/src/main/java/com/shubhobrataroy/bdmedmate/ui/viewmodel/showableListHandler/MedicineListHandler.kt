package com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler

import android.util.Log
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData

class MedicineListHandler constructor(
    repository: Repository,
    country: Country = Country.Bangladesh,
    isAscOrder : Boolean = true
) : ShowableListHandler(repository,country, "Medicine", isAscOrder) {

    override suspend fun getAllShowableLists(): ShowableListData {
        Log.d("MEDLOG","Med Repo Search")
        val list = repository.getAllMedicinesByCountry(
            "",
            country,isAscOrder
        )

        lastSuccessfulSearchQuery = ""
        return ShowableListData.MedicineShowableListData(list = list)
    }

    override suspend fun searchItemFromRepo(searchQuery: String): ShowableListData {
        val allShowableLists = getAllShowableLists()
        lastSuccessfulSearchQuery = searchQuery
        return allShowableLists
    }

    override suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData {
        Log.d("MEDLOG","Med Local Search")
        return if (showableListData is ShowableListData.MedicineShowableListData) {

            val list = showableListData.list.filter {
                it.name.contains(
                    searchQuery,
                    true
                )
            }
            lastSuccessfulSearchQuery = searchQuery
            ShowableListData.MedicineShowableListData(list)
        } else showableListData
    }


}