package com.shubhobrataroy.bdmedmate.presenter

import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

sealed class ListData
{
    data class MedicineListData(val list: List<Medicine>):ListData()
    data class MedicineGenericListData(val list: List<MedGeneric>) : ListData()


    fun isEmpty() =
        when(this)
        {
            is MedicineGenericListData -> list.isEmpty()
            is MedicineListData -> list.isEmpty()
        }

}
