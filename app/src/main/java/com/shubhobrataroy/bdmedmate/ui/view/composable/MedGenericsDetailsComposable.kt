package com.shubhobrataroy.bdmedmate.ui.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.ui.view.CommonTitle
import com.shubhobrataroy.bdmedmate.ui.view.MedGenericView

@Composable
fun RelatedMedicines(
    modifier: Modifier,
    medicines: List<Medicine>,
    onMedicineClick: (Medicine) -> Unit
) {
    Column(modifier = modifier) {
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
    generic: Generic,
    onSimilarMedicineClick: (Medicine) -> Unit
) {
    MedMateTheme {
        Card(elevation = 0.dp) {
            LazyColumn {
                item {
                    MedGenericView(
                        generic = generic,
                        true,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                item {
                    val similarMeds = generic.medicines?.collectAsState(initial = emptyList())
                    RelatedMedicines(
                        modifier = Modifier.padding(horizontal =8.dp),
                        similarMeds?.value.orEmpty(),
                        onSimilarMedicineClick
                    )
                }
            }

        }
    }
}