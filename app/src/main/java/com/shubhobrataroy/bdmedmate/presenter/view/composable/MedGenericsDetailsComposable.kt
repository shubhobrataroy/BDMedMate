package com.shubhobrataroy.bdmedmate.presenter.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.presenter.view.CommonTitle
import com.shubhobrataroy.bdmedmate.presenter.view.MedGenericView
import kotlinx.coroutines.flow.onEmpty

@Composable
fun RelatedMedicines(medicines: List<Medicine>, onMedicineClick: (Medicine) -> Unit) {
    Column {
        CommonTitle(title = "Medicines")
        if (medicines.isEmpty()) Text(text = "No medicines found")
        else LazyRow {
            items(medicines ?: return@LazyRow)
            {
                SimilarMedView(currentData = it, onMedicineClick)
            }
        }
    }
}





@Composable
fun MedGenericsDetailsComposable(
    medGeneric: MedGeneric,
    onSimilarMedicineClick: (Medicine) -> Unit
) {
    MedMateTheme {
        Card(elevation = 0.dp) {
            Column {
                MedGenericView(
                    medGeneric = medGeneric,
                    true,
                    modifier = Modifier.padding(8.dp)
                )


                val similarMeds = medGeneric.medicines?.collectAsState(initial = emptyList())

                RelatedMedicines(similarMeds?.value.orEmpty(),onSimilarMedicineClick)
            }

        }
    }
}