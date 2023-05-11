package com.shubhobrataroy.bdmedmate.ui.view.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.ui.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.ui.view.CommonDivider
import com.shubhobrataroy.bdmedmate.ui.view.CommonTitle
import com.shubhobrataroy.bdmedmate.ui.view.MedGenericView
import com.shubhobrataroy.bdmedmate.ui.viewmodel.MedicineListViewModel

@Composable
fun MedicineView(
    medicine: Medicine,
    viewModel: MedicineListViewModel = hiltViewModel(),
    onSimilarMedicineClick: (Medicine) -> Unit
) {

    val listState = rememberLazyListState()
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 6.dp),

        ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            item {
                Row {
                    Text(text = medicine.name, style = MaterialTheme.typography.h4)
                    if (medicine.type != null)
                        Text(
                            text = medicine.type,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp),
                            color = CurrentColorPalette.secondary
                        )
                }
                if (medicine.strength != null)
                    Text(
                        text = medicine.strength,
                        color = CurrentColorPalette.secondary, fontSize = 12.sp
                    )
            }

            item {
                val genericData :Generic? =medicine.generic?.collectAsState(initial = null)?.value
                val companyDetails =medicine.companyDetails?.collectAsState(initial = null)?.value

                ItemExtraData(
                    genericData,
                    companyDetails
                )
            }



            item {
                CommonTitle(title = "Similar Medicines")

                if (medicine.similarMedicines==null) return@item Text(text = "No similar medicines found")

                val similarMeds by remember{medicine.similarMedicines}.collectAsState(initial = emptyList())

                LazyRow {
                    items(similarMeds)
                    { item ->
                        SimilarMedView(
                            currentData = item,
                            onSimilarMedicineClick
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ItemExtraData(
    genericsEntity: Generic?,
    company: Company?
) {

    Column {
        CommonDivider()

        if (company != null)
            Text(text = company.name)

        if (genericsEntity != null)
            MedGenericView(generic = genericsEntity)


        Spacer(
            modifier = Modifier
                .height(24.dp),
        )

    }
}




@Composable
fun SimilarMedView(currentData: Medicine, onSimilarMedClicked: (Medicine) -> Unit) {
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CurrentColorPalette.primary),
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .width(IntrinsicSize.Max)
            .clickable {
                onSimilarMedClicked(currentData)
            }
    ) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = currentData.name + " ${currentData.strength ?: ""}",
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
fun MedicineDetailsComposable(
    medicineEntity: Medicine,
    viewModel: MedicineListViewModel = hiltViewModel(),
    onSimilarMedicineClick: (Medicine) -> Unit
) {
    Card(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
        MedicineView(medicineEntity, viewModel, onSimilarMedicineClick)
    }
}