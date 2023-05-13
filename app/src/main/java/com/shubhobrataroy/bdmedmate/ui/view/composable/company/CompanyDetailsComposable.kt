package com.shubhobrataroy.bdmedmate.ui.view.composable.company

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.ui.view.CommonDivider
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompanyDetailsComposable(
    modifier: Modifier = Modifier,
    company: Company,
    onMedClicked: (medicine: Medicine) -> Unit
) {
    val companyMeds by company.medicines.collectAsState(initial = null)
    LazyColumn(modifier) {

        stickyHeader {
            Column(modifier = Modifier.padding(vertical =  16.dp)) {
                Text(
                    text = company.name,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.secondary
                )
                CommonDivider()
            }
        }


        if (companyMeds.isNullOrEmpty()) {
            item {
                Text(text = "No similar medicines found")
            }
        } else {
            items(companyMeds.orEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMedClicked(it) },
                    shape = RoundedCornerShape(2.dp),

                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = it.name,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.h6
                        )
                        val generic =
                            it.generic?.collectAsState(null)?.value
                        if (generic != null)
                            Text(
                                text = generic.name,
                                textAlign = TextAlign.Start
                            )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}


fun LazyItemScope.addMedsToListView() {

}


@Preview
@Composable
fun CompanyDetailsPreview() {
    val medicine = Medicine(
        "ABC Medicine", "Syrup", "250 mg",
        generic = flowOf(Generic("Azythomycin"))
    )

    val company = Company("Test Company", flowOf(listOf(medicine, medicine, medicine)))

    MedMateTheme {
        CompanyDetailsComposable(company = company) {

        }
    }
}