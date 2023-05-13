package com.shubhobrataroy.bdmedmate.ui.view.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.ui.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme

@Composable
fun CompanyItemView(company: Company, onItemClicked: (company: Company) -> Unit) {
    Column(Modifier.clickable {
        onItemClicked(company)
    }) {
        Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Text(
                text = company.name, fontSize = 18.sp, modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 16.dp,
                ), fontWeight = FontWeight.Bold,
                color = CurrentColorPalette.secondary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CompanyListViewComposable(list: List<Company>, onItemClicked: (company: Company) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 4.dp,
            vertical = 8.dp
        )
    )
    {
        items(list) {
            CompanyItemView(it, onItemClicked)
        }
    }
}

@Preview
@Composable
fun CompanyListViewPreview(){
    val company = Company("Test Company")
    val list = ArrayList<Company>()

    for(i in 0 until 10)
        list.add(company.copy(name = company.name +" $i"))

    MedMateTheme {
        CompanyListViewComposable(list = list){

        }
    }
}