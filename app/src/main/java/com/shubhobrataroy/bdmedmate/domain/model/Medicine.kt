package com.shubhobrataroy.bdmedmate.domain.model

import kotlinx.coroutines.flow.Flow

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
data class Medicine(
    var name: String,

    val type: String? = null,

    val strength: String? = null,

    val genericName: String? = null,

    val companyName: String? = null,

    val genericFetcher: suspend () -> MedGeneric? = { null },

    val similarMedicines: Flow<List<Medicine>>? = null,

    val companyDetails: suspend () -> Company? = { null }
)
