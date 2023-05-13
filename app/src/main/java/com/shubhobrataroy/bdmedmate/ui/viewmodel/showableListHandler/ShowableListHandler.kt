package com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler

import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData

abstract class ShowableListHandler(
    protected val repository: Repository,
    protected val country: Country = Country.Bangladesh,
    protected var optionName: String,
    protected var isAscOrder: Boolean = true
) {

    protected var lastSuccessfulSearchQuery = ""

    abstract suspend fun getAllShowableLists(): ShowableListData

    abstract suspend fun searchItemFromRepo(searchQuery: String): ShowableListData

    abstract suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData

    suspend fun doIntelligentSearch(
        searchQuery: String,
        lastSearchResult: CommonState<ShowableListData>?
    ): ShowableListData {

        return if (lastSearchResult is CommonState.Success && shouldDoLocalSearch(
                searchQuery,
                lastSuccessfulSearchQuery,
                lastSearchResult.data.isEmpty()
            )
        ) {
            searchItemLocally(searchQuery, lastSearchResult.data)
        } else {
            searchItemFromRepo(searchQuery)
        }
    }

    private fun shouldDoLocalSearch(
        currentQuery: String,
        lastSuccessfulQuery: String,
        isListEmpty: Boolean
    ): Boolean {
        return isListEmpty.not() && lastSuccessfulQuery.isNotEmpty() && currentQuery.contains(
            lastSuccessfulQuery,true
        )
    }


    fun setNewListOrder(isAsc: Boolean) {
        this.isAscOrder = isAsc
    }
}