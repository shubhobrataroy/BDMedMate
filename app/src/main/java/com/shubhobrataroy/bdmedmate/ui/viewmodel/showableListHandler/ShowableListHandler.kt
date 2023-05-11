package com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler

import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData

abstract class ShowableListHandler(protected val repository: Repository,
                                   protected val country: Country = Country.Bangladesh,
                                   protected var optionName:String,
                                   protected var isAscOrder : Boolean = true) {
    abstract suspend fun getAllShowableLists(): ShowableListData

    abstract suspend fun searchItemFromRepo(searchQuery: String): ShowableListData

    abstract suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData

    abstract suspend fun doIntelligentSearch(
        searchQuery: String,
        commonState: CommonState<ShowableListData>?
    ): ShowableListData?

    abstract suspend fun <T> shouldDoLocalSearch(currentQuery:String,lastSuccessfulQuery:String,list: List<T>):Boolean


    abstract suspend fun setNewListOrder(isAsc:Boolean)
}