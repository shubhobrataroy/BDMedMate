package com.shubhobrataroy.bdmedmate.ui

import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

sealed class ShowableListData
{
    data class MedicineShowableListData(val list: List<Medicine>):ShowableListData()
    data class MedicineGenericShowableListData(val list: List<Generic>) : ShowableListData()
    data class CompanyShowableListData(val list: List<Company>) : ShowableListData()


    fun isEmpty() =
        when(this)
        {
            is MedicineGenericShowableListData -> list.isEmpty()
            is MedicineShowableListData -> list.isEmpty()
            is CompanyShowableListData -> list.isEmpty()
        }

}
