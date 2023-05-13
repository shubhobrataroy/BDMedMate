package com.shubhobrataroy.bdmedmate.ui.view.model

import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine

sealed class DashboardBottomSheetState {
    data class MedicineState(val medicine: Medicine) : DashboardBottomSheetState()
    data class GenericState(val medicineGeneric: Generic) : DashboardBottomSheetState()
    data class CompanyState(val company: Company) : DashboardBottomSheetState()
    object NoData : DashboardBottomSheetState()
}
