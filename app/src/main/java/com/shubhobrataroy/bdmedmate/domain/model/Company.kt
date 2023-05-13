package com.shubhobrataroy.bdmedmate.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
data class Company(
    val name: String,
    val medicines: Flow<List<Medicine>> = emptyFlow()
)