package com.shubhobrataroy.bdmedmate.data.bdDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_name")
data class CompanyEntity(
    @PrimaryKey
    @ColumnInfo(name = "company_id")
    var companyId:String,
    @ColumnInfo(name = "company_name")
    var companyName:String?,
    @ColumnInfo(name = "company_order")
    var companyOrder:String?,
    )