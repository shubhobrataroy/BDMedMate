package com.shubhobrataroy.bdmedmate.data.bdDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indication_generic_index")
data class GenericIndicationEntity(

    @ColumnInfo(name = "generic_id")
    var genericId: String?,
    @ColumnInfo(name = "indication_id")
    var indicationId: String?,

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    )
