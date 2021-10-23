package com.shubhobrataroy.bdmedmate.data.bd.mapper

import androidx.room.Embedded
import com.shubhobrataroy.bdmedmate.data.bd.entity.MedGenericsEntity

data class GenericIndicationsMapper(
    @Embedded
    val medGenericsEntity: MedGenericsEntity,

    
)
