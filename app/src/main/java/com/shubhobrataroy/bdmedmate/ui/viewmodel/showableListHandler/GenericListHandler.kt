package com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler

import android.util.Log
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData

class GenericListHandler(
    repository: Repository,
    country: Country = Country.Bangladesh,
    isAscOrder : Boolean = true
) : ShowableListHandler(repository,country, "Generic", isAscOrder) {

    private var searchQuery = ""

    private var lastSuccessfulSearchQuery = ""

    override suspend fun getAllShowableLists(): ShowableListData {
       return ShowableListData.MedicineGenericShowableListData(repository.getAllGenerics(country= country,searchQuery = searchQuery,byNameAsc = isAscOrder))
    }

    override suspend fun searchItemFromRepo(searchQuery: String): ShowableListData {
        Log.d("GenericSearch","Repo Search")
        this.searchQuery = searchQuery
        val optionDataByPresets = getAllShowableLists()
        lastSuccessfulSearchQuery = searchQuery
        return  optionDataByPresets
    }

    override suspend fun searchItemLocally(
        searchQuery: String,
        showableListData: ShowableListData
    ): ShowableListData {
        Log.d("MEDLOG","Generic Local Search")
        this.searchQuery = searchQuery
        val optionDataByPresets = getAllShowableLists()
        lastSuccessfulSearchQuery = searchQuery
        return  optionDataByPresets
    }

    override suspend fun doIntelligentSearch(
        searchQuery: String,
        commonState: CommonState<ShowableListData>?
    ): ShowableListData? {
        Log.d("MEDLOG","Generic Intelligent Search")
       return if(commonState!=null && commonState is CommonState.Success)
       {
           if(commonState.data is ShowableListData.MedicineGenericShowableListData)
           {
               if(shouldDoLocalSearch(searchQuery,lastSuccessfulSearchQuery,commonState.data.list))
                   searchItemLocally(searchQuery,commonState.data)
               else searchItemFromRepo(searchQuery)
           }
           else getAllShowableLists()
       }else null
    }

    override suspend fun <T> shouldDoLocalSearch(
        currentQuery: String,
        lastSuccessfulQuery: String,
        list: List<T>
    ): Boolean {
       return currentQuery.isNotEmpty() &&
                lastSuccessfulSearchQuery.isNotEmpty() &&
                list.isNotEmpty()
    }



    override suspend fun setNewListOrder(isAsc: Boolean) {
        this.isAscOrder = isAsc
    }
}