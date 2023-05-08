package com.shubhobrataroy.bdmedmate.domain

import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import com.shubhobrataroy.bdmedmate.ui.CommonState
import java.lang.Exception

/**
 * Created by shubhobrataroy on 20,October,2021
 **/


suspend fun <T : Any> MutableLiveData<CommonState<T>>.execCatching(task: suspend () -> T?): CommonState<T> {
    postValue(CommonState.Fetching)

    return try {
        CommonState.Success(task()?: throw Exception("No Data")).apply {
            postValue(this)
        }

    } catch (ex: Exception) {
        CommonState.Error(ex).apply {
            postValue(this)
        }
    }
}

suspend fun <T : Any> LiveDataScope<CommonState<T>>.wrapWithState(task: suspend () -> T?) {
    emit(CommonState.Fetching)

    emit(
        task().run {
            if (this == null) CommonState.Error(NoSuchElementException())
            else CommonState.Success(this)
        })
}