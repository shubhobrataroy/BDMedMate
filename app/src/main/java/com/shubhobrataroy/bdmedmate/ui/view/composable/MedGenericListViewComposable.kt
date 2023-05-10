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
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.ui.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme


@Composable
fun MedicineGenericListView(list: List<MedGeneric>, onItemClicked: (medGeneric:MedGeneric ) ->Unit)
{
    LazyColumn(contentPadding = PaddingValues(horizontal = 4.dp,
        vertical = 8.dp))
    {
        items(list) {
            MedicineGenericItemView(medGeneric = it,onItemClicked)
        }
    }
}


@Composable
fun MedicineGenericItemView(medGeneric: MedGeneric ,  onItemClicked: ( medGeneric:MedGeneric ) ->Unit)
{
    Column (Modifier.clickable {
        onItemClicked(medGeneric)
    }){
        Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Text(
                text = medGeneric.name, fontSize = 18.sp, modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 16.dp,
                ),fontWeight = FontWeight.Bold,
                color = CurrentColorPalette.secondary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MedGenerics() {

    val medGeneric = MedGeneric("Azythomycin")
    MedMateTheme {
        MedicineGenericListView(listOf(medGeneric,medGeneric),){}
    }
}



