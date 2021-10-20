package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.viewmodel.MedicineListViewModel
import com.shubhobrataroy.bdmedmate.ui.theme.BDMedMateIndianMedicineToBangladeshiMedicineTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MedicineListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BDMedMateIndianMedicineToBangladeshiMedicineTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MedicineDataView(viewModel = viewModel)
                }
            }
        }

        viewModel.getMedicineList()
    }

    @Composable
    fun MedicineDataView(viewModel: MedicineListViewModel) {
        val state by viewModel.medListLiveData.observeAsState(CommonState.Idle)

        MedicineListView(state)
    }

    @Composable
    fun ItemExtraData(medicine: Medicine) {
        val extraData by
        viewModel.getGenericsAndCompanyDetails(medicine).observeAsState(null)

        if (extraData == null) return

        Column {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .padding(horizontal = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )

            if (extraData?.first != null)
                Text(
                    text = extraData?.first?.name ?: "",
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

            if (extraData?.second != null)
                Text(text = extraData?.second?.name ?: "")

            Spacer(
                modifier = Modifier
                    .height(24.dp),
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onClick = { },
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
                            color = Color.DarkGray
                        )
                }
                if (medicine.strength != null)
                    Text(
                        text = medicine.strength,
                        color = Color.DarkGray, fontSize = 12.sp
                    )


                AnimatedVisibility(visible = clicked) {
                    ItemExtraData(medicine = medicine)
                }


            }
        }
    }

    @Composable
    fun MedicineListView(medicineListState: CommonState<List<Medicine>>) {
        when (medicineListState) {
            is CommonState.Error -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "List Fetching error " + medicineListState.exception.message)
            }


            CommonState.Fetching ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            is CommonState.Success -> LazyColumn(

                contentPadding = PaddingValues(
                    vertical = 4.dp,
                    horizontal = 8.dp
                )
            ) {
                items(medicineListState.data.size) { index ->
                    MedicineItemView(medicine = medicineListState.data[index])
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            CommonState.Idle -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Nothing Here")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        val med = Medicine("Test Medicine", "Tablet", "100 mg")

        val meds = ArrayList<Medicine>().apply {
            for (i in 0 until 100)
                add(med)
        }

        BDMedMateIndianMedicineToBangladeshiMedicineTheme {
            MedicineListView(medicineListState = CommonState.Success(meds))
        }
    }
}

