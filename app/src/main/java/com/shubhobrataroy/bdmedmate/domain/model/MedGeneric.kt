package com.shubhobrataroy.bdmedmate.domain.model

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
data class MedGeneric(
    val name: String,
    val medicines:()-> List<Medicine> = { emptyList()}
)