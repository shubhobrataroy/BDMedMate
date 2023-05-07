package com.shubhobrataroy.bdmedmate.presenter.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.domain.execCatching
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.domain.wrapWithState
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ShowableListData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
@HiltViewModel
class MedicineListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val country = Country.Bangladesh

    private val options: List<Options> by lazy {
        arrayListOf(
            MedicineListOption(repository, country),
            GenericListOption(repository, country, ),
            MedicineListOption(repository, country, ),
        )
    }

    @Volatile
    private var currentlySelectedOption = options[0]


    private val _selectedCategoryItemList =
        MutableLiveData<CommonState<ShowableListData>>(CommonState.Idle)

    val selectedCategoryItemShowableList: LiveData<CommonState<ShowableListData>> =
        _selectedCategoryItemList

    private var searchJob: Deferred<String>? = null

    private var listAsc: Boolean = true

    val searchQueryState = mutableStateOf("")



    fun getGenericsAndCompanyDetails(medicine: Medicine) =
        liveData(Dispatchers.IO) {
            this.wrapWithState {
                medicine.genericFetcher() to medicine.companyDetails()
            }
        }

    fun onListOrderSelected(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listAsc = index ==0
            currentlySelectedOption.setNewListOrder(listAsc)
            _selectedCategoryItemList.execCatching {
                currentlySelectedOption.searchItemFromRepo(searchQueryState.value)
            }
        }
    }


//    private fun performLocalSearch(query: String, existingList: List<Medicine>) {
//        searchJob = viewModelScope.async(Dispatchers.Default) {
//            _medListLiveData.execCatching {
//                ShowableListData.MedicineShowableListData(existingList.filter {
//                    it.name.contains(
//                        query,
//                        true
//                    )
//                })
//            }
//            query
//        }
//    }

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
                searchQuery = searchQuery,
                _selectedCategoryItemList.value
            ) ?: currentlySelectedOption.getOptionDataByPresets()
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
            currentlySelectedOption = options[index]
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
                    getOptionDataByPresets()
                }
           }
       }
    }
}