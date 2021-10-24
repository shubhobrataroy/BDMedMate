package com.shubhobrataroy.bdmedmate.presenter.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.domain.execCatching
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.domain.wrapWithState
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ListData
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

    private val _medListLiveData = MutableLiveData<CommonState<ListData.MedicineListData>>().apply {
        postValue(CommonState.Idle)
    }
    val medListLiveData: LiveData<CommonState<ListData.MedicineListData>> = _medListLiveData

    private val _selectedCategoryItemList =
        MutableLiveData<CommonState<ListData>>(CommonState.Idle)

    val selectedCategoryItemList: LiveData<CommonState<ListData>> = _selectedCategoryItemList

    private var searchJob: Deferred<String>? = null

    private var medicineListAsc: Boolean = true

    val searchQueryState = mutableStateOf("")

    fun fetchMedicineList() {
        searchJob = viewModelScope.async(Dispatchers.IO) {
            val lastQuery = searchQueryState.value
            _selectedCategoryItemList.execCatching {
                ListData.MedicineListData(
                    repository.getAllMedicinesByCountry(
                        lastQuery,
                        country,
                        medicineListAsc
                    )
                )

            }
            lastQuery
        }
    }

    fun getGenericsAndCompanyDetails(medicine: Medicine) =
        liveData(Dispatchers.IO) {
            this.wrapWithState {
                medicine.genericFetcher() to medicine.companyDetails()
            }
        }

    fun onMedicineOrderSelected(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineListAsc = index == 0
            fetchMedicineList()
        }
    }


    private fun performLocalSearch(query: String, existingList: List<Medicine>) {
        searchJob = viewModelScope.async(Dispatchers.Default) {
            _medListLiveData.execCatching {
                ListData.MedicineListData(existingList.filter { it.name.contains(query, true) })
            }
            query
        }
    }

    private fun shouldDoLocalSearch(latestQuery: String, oldQuery: String) =
        oldQuery.isNotEmpty() && latestQuery.startsWith(oldQuery) && _selectedCategoryItemList.value.run {
            if (this == null) return@run false
            if (this is CommonState.Success) !this.data.isEmpty()
            else false
        }


    private suspend fun requestSearchIfRequired(searchQuery: String) {

        if (shouldDoLocalSearch(searchQuery, searchQueryState.value)) {
            this.searchQueryState.value = searchQuery
            performLocalSearch(searchQuery, _medListLiveData.getCalculatedValue())
        } else {
            this.searchQueryState.value = searchQuery
            fetchMedicineList()
        }
        val latestQuery = this.searchQueryState.value
        val lastQuery = searchJob?.await() ?: ""
        if (latestQuery != lastQuery)
            requestSearchIfRequired(latestQuery)
    }

    fun searchMedicine(searchQuery: String) {


        if (searchJob?.isCompleted == false) {
            this.searchQueryState.value = searchQuery
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            requestSearchIfRequired(searchQuery)
        }
    }


    private fun MutableLiveData<CommonState<ListData.MedicineListData>>.getCalculatedValue(): List<Medicine> {
        val value = value
        return if (value is CommonState.Success) value.data.list
        else emptyList()
    }

    fun fetchSimilarMeds(medicine: Medicine) =
        liveData<CommonState<List<Medicine>>>(Dispatchers.IO) {
            wrapWithState { medicine.similarMedicines() }
        }

   suspend fun fetchGenericList()
    {
        _selectedCategoryItemList.execCatching {
           ListData.MedicineGenericListData( repository.getAllGenerics())
        }
    }

    fun selectCategory(index: Int) {
        viewModelScope.launch(Dispatchers.IO){
            when (index) {
                0 -> fetchMedicineList()
                1 -> fetchGenericList()
            }
        }
    }
}