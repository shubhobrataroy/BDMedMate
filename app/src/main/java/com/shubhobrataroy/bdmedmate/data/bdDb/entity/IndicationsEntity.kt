package com.shubhobrataroy.bdmedmate.data.bdDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indication")
data class IndicationsEntity(
    @PrimaryKey
    @ColumnInfo(name = "indication_id")
    var indicationId: String,
    @ColumnInfo(name = "indication_name")
    var indicationName: String?,
)
