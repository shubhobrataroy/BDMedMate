package com.shubhobrataroy.bdmedmate.presenter.viewmodel

import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.Repository
import com.shubhobrataroy.bdmedmate.domain.execCatching
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
@HiltViewModel
class MedicineListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _medListLiveData = MutableLiveData<CommonState<List<Medicine>>>().apply {
        postValue(CommonState.Idle)
    }
    val medListLiveData: LiveData<CommonState<List<Medicine>>> = _medListLiveData


    fun getMedicineList(country: Country = Country.Bangladesh) {
        viewModelScope.launch(Dispatchers.IO) {
            _medListLiveData.execCatching {
                repository.getAllMedicinesByCountry(country)
            }
        }
    }

    fun getGenericsAndCompanyDetails(medicine: Medicine) =
        liveData(Dispatchers.IO) {
            emit(medicine.genericFetcher() to medicine.companyDetails())
        }

}