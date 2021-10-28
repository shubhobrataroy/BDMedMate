package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

@ExperimentalFoundationApi
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

    @ExperimentalFoundationApi
    @Composable
    private fun RelatedMedicines(medicinesState: State<List<Medicine>?>) {
        val medicines by remember { medicinesState }

        Column {
            CommonTitle(title = "Medicines")
            if (medicines.isNullOrEmpty()) Text(text = "No medicines found")
            else LazyRow {
                items(medicines ?: return@LazyRow)
                {
                    SimilarMedView(currentData = it, fragmentManager = childFragmentManager)
                }
            }
        }
    }

    private fun getSimilarMeds ( generic: MedGeneric) = liveData(Dispatchers.IO) {
        emit(generic.medicines())
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
                        Column {
                            MedGenericView(
                                medGeneric = medGeneric,
                                true,
                                modifier = Modifier.padding(8.dp)
                            )
                            RelatedMedicines(medicinesState = getSimilarMeds(medGeneric).observeAsState(
                                initial = null
                            ))
                        }

                    }
                }
            }
        }
    }
}