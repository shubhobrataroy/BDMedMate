package com.shubhobrataroy.bdmedmate.data.bdDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generic")
data class MedGenericsEntity(
    @PrimaryKey
    @ColumnInfo(name = "generic_id")
    var genericId:String,
    @ColumnInfo(name = "generic_name")
    var genericName:String? = null,
    @ColumnInfo(name = "precaution")
    var precaution:String? = null,
    @ColumnInfo(name = "indication")
    var indication:String? = null,
    @ColumnInfo(name = "contra_indication")
    var contraIndication:String? = null,
    @ColumnInfo(name = "dose")
    var dose:String? = null,
    @ColumnInfo(name = "side_effect")
    var sideEffect:String? = null,
    @ColumnInfo(name = "pregnancy_category_id")
    var pregnancyCategoryId:String? = null,
    @ColumnInfo(name = "mode_of_action")
    var modeOfAction:String? = null,
    @ColumnInfo(name = "interaction")
    var interaction:String? = null,
)