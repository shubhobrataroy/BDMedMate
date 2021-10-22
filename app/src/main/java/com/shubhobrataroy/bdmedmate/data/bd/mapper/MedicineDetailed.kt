package com.shubhobrataroy.bdmedmate.data.bd.mapper

import androidx.room.Embedded
import androidx.room.Relation
import com.shubhobrataroy.bdmedmate.data.bd.entity.CompanyEntity
import com.shubhobrataroy.bdmedmate.data.bd.entity.MedGenericsEntity
import com.shubhobrataroy.bdmedmate.data.bd.entity.MedicineEntity

/**
 * Created by shubhobrataroy on 22,October,2021
 **/
data class MedicineDetailed(
    @Embedded val medicine: MedicineEntity,

    @Relation(
        parentColumn = "brand_id",
        entityColumn = "generic_id"
    )
    val genericsEntity: MedGenericsEntity?,

    @Relation(
        parentColumn = "brand_id",
        entityColumn = "company_id"
    )
    val companyEntity: CompanyEntity?,


)