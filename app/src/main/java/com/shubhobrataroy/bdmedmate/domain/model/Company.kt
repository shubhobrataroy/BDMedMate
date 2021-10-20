package com.shubhobrataroy.bdmedmate.domain.model

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
class Company(
    val name: String,
    val medicines: suspend () -> List<Medicine> = { emptyList() }
)