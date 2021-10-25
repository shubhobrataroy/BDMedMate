package com.shubhobrataroy.bdmedmate.presenter.viewmodel

import com.shubhobrataroy.bdmedmate.domain.Repository
import javax.inject.Inject

class ListOptions @Inject constructor(private val repository: Repository) {
    val optionList = arrayListOf(
        "Medicine",
        "Generic",
        "Brand"
    )


    var selectedOption = 0


}

