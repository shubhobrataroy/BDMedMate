package com.shubhobrataroy.bdmedmate.domain

import androidx.lifecycle.MutableLiveData
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import java.lang.Exception

/**
 * Created by shubhobrataroy on 20,October,2021
 **/


suspend fun <T : Any> MutableLiveData<CommonState<T>>.execCatching(task: suspend ()-> T)
{
    postValue(CommonState.Fetching)

    try {
        postValue(CommonState.Success(task()))
    }catch (ex:Exception)
    {
        postValue(CommonState.Error(ex))
    }
}