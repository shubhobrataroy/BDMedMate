package com.shubhobrataroy.bdmedmate.domain

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
sealed class Country(val locale:String) {
   object Bangladesh :Country("bn")
   object India :Country("in")
}