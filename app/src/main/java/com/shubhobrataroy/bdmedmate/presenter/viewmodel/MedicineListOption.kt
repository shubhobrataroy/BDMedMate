package com.shubhobrataroy.bdmedmate.presenter.viewmodel

import android.util.Log
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ShowableListData

class MedicineListOption constructor(
    repository: Repository,
    country: Country = Country.Bangladesh,
    isAscOrder : Boolean = true
) : Options(repository,country, "Medicine", isAscOrder) {
    private var searchQuery = ""

    private var lastSuccessfulSearchQuery = ""

    override suspend fun getOptionDataByPresets(): ShowableListData {
        Log.d("MEDLOG","Med Repo Search")
        val list = repository.getAllMedicinesByCountry(
            searchQuery,
            country,isAscOrder
        )

        lastSuccessfulSearchQuery = searchQuery
        return ShowableListData.MedicineShowableListData(list = list)
    }

    override suspend fun searchItemFromRepo(searchQuery: String): ShowableListData {
        this.searchQuery = searchQuery
        return getOptionDataByPresets()
    }

    override suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData {
        Log.d("MEDLOG","Med Local Search")
        this.searchQuery = searchQuery
        return if (showableListData is ShowableListData.MedicineShowableListData) {

            val list = showableListData.list.filter {
                it.name.contains(
                    searchQuery,
                    true
                )
            }
            lastSuccessfulSearchQuery = this.searchQuery
            ShowableListData.MedicineShowableListData(list)
        } else showableListData
    }

    override suspend fun setNewListOrder(isAsc: Boolean) {
        isAscOrder = isAsc
    }


    override suspend fun doIntelligentSearch(
        searchQuery: String,
        commonState: CommonState<ShowableListData>?
    ): ShowableListData {
        Log.d("MEDLOG","Med Repo Search")
        return when (commonState) {
            is CommonState.Error, null, CommonState.Idle, CommonState.Fetching -> getOptionDataByPresets()
            is CommonState.Success -> {
                if (commonState.data is ShowableListData.MedicineShowableListData) {
                    val list = commonState.data.list
                    this.searchQuery = searchQuery
                    if(shouldDoLocalSearch(searchQuery,lastSuccessfulSearchQuery,list))
                        searchItemLocally(searchQuery,commonState.data)
                    else searchItemFromRepo(searchQuery)
                }
                else searchItemFromRepo(searchQuery)
            }
        }
    }

    override suspend fun <T> shouldDoLocalSearch(
        currentQuery: String,
        lastSuccessfulQuery: String,
        list: List<T>
    ): Boolean {
        return list.isNotEmpty() && lastSuccessfulQuery.isNotEmpty() && currentQuery.contains(lastSuccessfulQuery)
    }



}