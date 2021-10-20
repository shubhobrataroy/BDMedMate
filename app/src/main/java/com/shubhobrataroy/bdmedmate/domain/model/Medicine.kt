package com.shubhobrataroy.bdmedmate.domain.model

import android.telecom.Call
import androidx.room.ColumnInfo

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
data class Medicine(
    var name: String,

    val type: String?=null,

    val strength: String? = null,


    val genericFetcher: suspend () -> MedGeneric? = { null },

    val similarMedicines: suspend () -> List<Medicine> = { emptyList() },

    val companyDetails: suspend () -> Company? = { null }
)
