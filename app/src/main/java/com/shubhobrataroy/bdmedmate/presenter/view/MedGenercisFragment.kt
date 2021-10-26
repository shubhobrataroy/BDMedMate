package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme

class MedGenercisFragment : BottomSheetDialogFragment() {

    companion object {
        private var INSTANCE: MedGenercisFragment? = null
        lateinit var medGeneric: MedGeneric
        fun getInstance(medGeneric: MedGeneric): MedGenercisFragment {
            this.medGeneric = medGeneric
            return if (INSTANCE != null) INSTANCE!!
            else MedGenercisFragment().apply {
                INSTANCE = this
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                MedMateTheme {
                    Card(elevation = 0.dp) {
                        MedGenericView(
                            medGeneric = medGeneric,
                            true,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}