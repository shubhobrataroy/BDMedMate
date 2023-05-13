package com.shubhobrataroy.bdmedmate.ui.view.composable.generic

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme


@Composable
fun MedicineGenericListView(list: List<Generic>, onItemClicked: (generic:Generic ) ->Unit)
{
    LazyColumn(contentPadding = PaddingValues(horizontal = 4.dp,
        vertical = 8.dp))
    {
        items(list) {
            MedicineGenericItemView(generic = it,onItemClicked)
        }
    }
}


@Composable
fun MedicineGenericItemView(generic: Generic, onItemClicked: (generic:Generic ) ->Unit)
{
    Column (Modifier.clickable {
        onItemClicked(generic)
    }){
        Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Text(
                text = generic.name, fontSize = 18.sp, modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 16.dp,
                ),fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MedGenerics() {

    val generic = Generic("Azythomycin")
    MedMateTheme {
        MedicineGenericListView(listOf(generic,generic),){}
    }
}



