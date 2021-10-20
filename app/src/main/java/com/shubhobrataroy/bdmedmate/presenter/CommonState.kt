package com.shubhobrataroy.bdmedmate.presenter

/**
 * Created by shubhobrataroy on 20,October,2021
 **/
sealed class CommonState<out T : Any> {
    object Fetching : CommonState<Nothing>()
    object Idle : CommonState<Nothing>()
    data class Error(val exception: Exception) : CommonState<Nothing>()
    data class Success<out T : Any>(val data: T) : CommonState<T>()
}
