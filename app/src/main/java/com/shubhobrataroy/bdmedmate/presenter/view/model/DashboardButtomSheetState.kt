package com.shubhobrataroy.bdmedmate.presenter.view.model

import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

sealed class DashboardBottomSheetState{
    data class MedicineState(val medicine: Medicine):DashboardBottomSheetState()
    data class GenericState(val medicineGeneric: MedGeneric):DashboardBottomSheetState()
    object NoData:DashboardBottomSheetState()
}
