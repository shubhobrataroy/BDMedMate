package com.shubhobrataroy.bdmedmate.ui

import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

sealed class ShowableListData
{
    data class MedicineShowableListData(val list: List<Medicine>):ShowableListData()
    data class MedicineGenericShowableListData(val list: List<MedGeneric>) : ShowableListData()


    fun isEmpty() =
        when(this)
        {
            is MedicineGenericShowableListData -> list.isEmpty()
            is MedicineShowableListData -> list.isEmpty()
        }

}
