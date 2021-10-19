package com.shubhobrataroy.bdmedmate.data.bdDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "brand")
data class MedicineEntity(
    @PrimaryKey
    @ColumnInfo(name = "brand_id")
    var brandID:String,
    @ColumnInfo(name = "generic_id")
    var genericId:String?,
    @ColumnInfo(name = "company_id")
    var companyId:String?,
    @ColumnInfo(name = "brand_name")
    var brandName:String?,
    @ColumnInfo(name = "form")
    var form:String?,
    @ColumnInfo(name = "strength")
    var strength:String?,
    @ColumnInfo(name = "price")
    var price:String?,
    @ColumnInfo(name = "packsize")
    var packSize:String?,

)
