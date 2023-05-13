package com.shubhobrataroy.bdmedmate.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.domain.execCatching
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData
import com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler.CompanyListHandler
import com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler.GenericListHandler
import com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler.MedicineListHandler
import com.shubhobrataroy.bdmedmate.ui.viewmodel.showableListHandler.ShowableListHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
@HiltViewModel
class MedicineListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val country = Country.Bangladesh

    private val listHandlers: List<ShowableListHandler> by lazy {
        arrayListOf(
            MedicineListHandler(repository, country),
            GenericListHandler(repository, country, ),
            CompanyListHandler(repository, country, ),
        )
    }

    @Volatile
    private var currentlySelectedOption = listHandlers[0]


    private val _selectedCategoryItemList =
        MutableLiveData<CommonState<ShowableListData>>(CommonState.Idle)

    val selectedCategoryItemShowableList: LiveData<CommonState<ShowableListData>> =
        _selectedCategoryItemList

    private var searchJob: Deferred<String>? = null

    private var listAsc: Boolean = true

    val searchQueryState = mutableStateOf("")




    fun onListOrderSelected(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listAsc = index ==0
            currentlySelectedOption.setNewListOrder(listAsc)
            _selectedCategoryItemList.execCatching {
                currentlySelectedOption.searchItemFromRepo(searchQueryState.value)
            }
        }
    }



    private fun shouldDoLocalSearch(latestQuery: String, oldQuery: String) =
        oldQuery.isNotEmpty() && latestQuery.startsWith(oldQuery) && _selectedCategoryItemList.value.run {
            if (this == null) return@run false
            if (this is CommonState.Success) !this.data.isEmpty()
            else false
        }


    private suspend fun requestSearchIfRequired(searchQuery: String) {

        this.searchQueryState.value = searchQuery

        _selectedCategoryItemList.execCatching {
            currentlySelectedOption.doIntelligentSearch(
                searchQuery = searchQuery,_selectedCategoryItemList.value
            )
        }

        val latestQuery = this.searchQueryState.value

        if (latestQuery != searchQuery)
            requestSearchIfRequired(latestQuery)
    }

    fun searchMedicine(searchQuery: String) {


        if (searchJob != null && searchJob?.isCompleted == false) {
            this.searchQueryState.value = searchQuery
            return
        }

        searchJob = viewModelScope.async(Dispatchers.IO) {
            requestSearchIfRequired(searchQuery)
            searchQuery
        }
    }


    private fun MutableLiveData<CommonState<ShowableListData.MedicineShowableListData>>.getCalculatedValue(): List<Medicine> {
        val value = value
        return if (value is CommonState.Success) value.data.list
        else emptyList()
    }

    fun selectCategory(index: Int) {
        Log.e("MEDLOG","on category selected")
        val value = searchQueryState.value
        searchJob = viewModelScope.async(Dispatchers.IO) {
            currentlySelectedOption = listHandlers[index]
            currentlySelectedOption.setNewListOrder(this@MedicineListViewModel.listAsc)
            _selectedCategoryItemList.execCatching {
                currentlySelectedOption.doIntelligentSearch(
                    searchQuery = value,
                    _selectedCategoryItemList.value
                )
            }
            value
        }
    }

    fun fetchSelectedOptionData() {
       viewModelScope.launch(Dispatchers.IO) {
           _selectedCategoryItemList.execCatching {
                currentlySelectedOption.run {
                    getAllShowableLists()
                }
           }
       }
    }
}