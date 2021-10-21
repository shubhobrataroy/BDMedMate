package com.shubhobrataroy.bdmedmate.domain.model

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
data class MedGeneric(
    val name: String,
    val indication:String?=null,
    val contraIndication:String?=null,
    val dosage:String?=null,
    val sideEffect:String?=null,
    val medicines:()-> List<Medicine> = { emptyList()},

)