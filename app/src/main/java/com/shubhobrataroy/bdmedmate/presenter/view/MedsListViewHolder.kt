package com.shubhobrataroy.bdmedmate.presenter.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.CurrentColorPalette

@ExperimentalAnimationApi
@ExperimentalFoundationApi
class MedsListViewHolder(private val onItemClicked: ((medicine: Medicine) -> Unit)? = null) {


    @Composable
    fun ItemExtraData(medicine: Medicine) {

        Column {
            Spacer(
                modifier = Modifier
                    .height(24.dp),
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onClick = {
                    onItemClicked?.let { it(medicine) }
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "View Details")
            }
        }
    }

    @Composable
    fun MedicineItemView(medicine: Medicine) {
        var clicked by remember {
            mutableStateOf(false)
        }

        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .clickable {
                    clicked = !clicked
                },
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                Row {
                    Text(text = medicine.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
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


                if (medicine.companyName != null) {
                    CommonDivider()
                    Text(
                        text = medicine.companyName,
                        color = CurrentColorPalette.secondaryVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (medicine.genericName != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = medicine.genericName,
                        fontStyle = FontStyle.Italic,
                        color = CurrentColorPalette.secondary
                    )
                }




                AnimatedVisibility(visible = clicked) {
                    ItemExtraData(medicine = medicine)
                }


            }
        }
    }


    @Composable
    fun MedicineListView(
        medicineListState: CommonState<List<Medicine>>,
        modifier: Modifier = Modifier
    ) {
        medicineListState.toComposable {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(
                    vertical = 4.dp,
                    horizontal = 8.dp
                )
            ) {
                items(it){ item ->
                    MedicineItemView(medicine = item)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}