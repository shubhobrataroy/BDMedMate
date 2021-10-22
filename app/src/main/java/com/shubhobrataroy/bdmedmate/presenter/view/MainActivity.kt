package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.fragment.app.FragmentActivity
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.presenter.viewmodel.MedicineListViewModel
import com.shubhobrataroy.bdmedmate.ui.theme.BDMedMateIndianMedicineToBangladeshiMedicineTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel by viewModels<MedicineListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedMateTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MedicineDataView(viewModel = viewModel)
                }
            }
        }

        viewModel.fetchMedicineList()
    }

    @Composable
    fun MedicineDataView(viewModel: MedicineListViewModel) {
        val state by viewModel.medListLiveData.observeAsState(CommonState.Idle)

        MedicineListPage(medicineListState = state)
    }


    @Composable
    fun SearchHeader() {
        Card(
            modifier = Modifier.padding(bottom = 16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Medicine", Modifier.fillMaxWidth())

                val searchState = remember {
                    viewModel.searchQueryState
                }
                OutlinedTextField(
                    value = searchState.value, onValueChange = {
                        viewModel.searchMedicine(it)
                    },
                    label = {
                        Text("Search by Medicine Name")
                    }, modifier = Modifier.fillMaxWidth(.95f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Order: ")
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomRadioGroup(arrayOf("A to Z", "Z to A")) { value, index ->
                        viewModel.onMedicineOrderSelected(index)
                    }
                }
            }
        }
    }

    @Composable
    fun MedicineListPage(medicineListState: CommonState<List<Medicine>>) {
        Column {
            SearchHeader()
            when (medicineListState) {
                is CommonState.Error -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "List Fetching error " + medicineListState.exception.message)
                }


                CommonState.Fetching -> CenterProgress()

                is CommonState.Success ->
                    MedicineListView(medicineListState = medicineListState)

                CommonState.Idle -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Nothing Here")
                }
            }
        }
    }

    @Composable
    fun ItemExtraData(medicine: Medicine) {
        val extraData by
        viewModel.getGenericsAndCompanyDetails(medicine).observeAsState(null)

        if (extraData == null) return

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
                    val medicineDetailsFragment =
                        MedicineDetailsFragment(
                            viewModel,
                            medicine,
                            extraData?.first,
                            extraData?.second
                        )
                    medicineDetailsFragment.show(supportFragmentManager, "Show")
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



                if (medicine.genericName != null) {
                    CommonDivider()
                    Text(
                        text = medicine.genericName,
                        fontStyle = FontStyle.Italic,
                        color = CurrentColorPalette.secondary
                    )
                }

                if (medicine.companyName != null) {
                    CommonDivider()
                    Text(
                        text = medicine.companyName,
                        color = CurrentColorPalette.secondaryVariant,
                        fontWeight = FontWeight.Bold
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
        medicineListState: CommonState.Success<List<Medicine>>,
        modifier: Modifier = Modifier
    ) {
        LazyColumn(
            modifier = modifier,
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
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        val med = Medicine("Test Medicine", "Tablet", "100 mg")

        val meds = ArrayList<Medicine>().apply {
            for (i in 0 until 100)
                add(med)
        }

        MedMateTheme {
            MedicineListPage(medicineListState = CommonState.Success(meds))
//            SearchHeader()
        }
    }
}


@Composable
fun CenterProgress() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Composable
fun CustomRadioGroup(options: Array<String>, onChanged: (value: String, index: Int) -> Unit) {

    val selected = remember { mutableStateOf(options[0]) }

    Row(
        Modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        options.forEachIndexed { index, value ->
            Row(Modifier.clickable {
                selected.value = value
                onChanged(value, index)
            }) {
                RadioButton(selected = value == selected.value, onClick = null)
                Text(
                    text = value,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 2.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}