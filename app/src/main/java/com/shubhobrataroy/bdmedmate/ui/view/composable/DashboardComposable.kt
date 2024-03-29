package com.shubhobrataroy.bdmedmate.ui.view.composable

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ShowableListData
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.ui.view.CommonDivider
import com.shubhobrataroy.bdmedmate.ui.view.FancyRadioGroup
import com.shubhobrataroy.bdmedmate.ui.view.composable.company.CompanyDetailsComposable
import com.shubhobrataroy.bdmedmate.ui.view.composable.company.CompanyListViewComposable
import com.shubhobrataroy.bdmedmate.ui.view.composable.generic.MedGenericsDetailsComposable
import com.shubhobrataroy.bdmedmate.ui.view.composable.generic.MedicineGenericListView
import com.shubhobrataroy.bdmedmate.ui.view.composable.medicine.MedicineDetailsComposable
import com.shubhobrataroy.bdmedmate.ui.view.composable.medicine.MedicineListView
import com.shubhobrataroy.bdmedmate.ui.view.model.DashboardBottomSheetState
import com.shubhobrataroy.bdmedmate.ui.view.toComposable
import com.shubhobrataroy.bdmedmate.ui.viewmodel.MedicineListViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardPage(viewModel: MedicineListViewModel = hiltViewModel()) {
    MedMateTheme {
        val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
        )
        val coroutineScope = rememberCoroutineScope()

        BackHandler(sheetState.isVisible) {
            coroutineScope.launch { sheetState.hide() }
        }

        var currentBottomSheetState: DashboardBottomSheetState by remember {
            mutableStateOf(
                DashboardBottomSheetState.NoData
            )
        }

        fun setState(state: DashboardBottomSheetState) {
            currentBottomSheetState = state
            coroutineScope.launch {
                if (state is DashboardBottomSheetState.NoData && sheetState.isVisible)
                    sheetState.hide()
                else if (sheetState.isVisible.not()) {
                    sheetState.show()
                }
            }
        }

        val medCallback: (Medicine) -> Unit = {
            setState(DashboardBottomSheetState.MedicineState(it))
        }

        val companyCallback: (Company) -> Unit = {
            setState(DashboardBottomSheetState.CompanyState(it))
        }



        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                when (currentBottomSheetState) {
                    is DashboardBottomSheetState.GenericState -> MedGenericsDetailsComposable(
                        generic = (currentBottomSheetState as DashboardBottomSheetState.GenericState).medicineGeneric,
                        medCallback
                    )

                    is DashboardBottomSheetState.MedicineState -> MedicineDetailsComposable(
                        medicineEntity = (currentBottomSheetState as DashboardBottomSheetState.MedicineState).medicine,
                        companyCallback,
                        medCallback
                    )

                    DashboardBottomSheetState.NoData -> {}
                    is DashboardBottomSheetState.CompanyState -> CompanyDetailsComposable(
                        company = (currentBottomSheetState as DashboardBottomSheetState.CompanyState).company,
                        onMedClicked = medCallback, modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        ) {
            Scaffold() {
                DashboardContent(
                    modifier = Modifier.padding(it),
                    viewModel = viewModel,
                    onMedicineDetailsRequested = { med ->
                        Log.d("MedDetails", med.toString())
                        setState(DashboardBottomSheetState.MedicineState(med))
                    },
                    onMedicineGenericDetailsRequested = { medGeneric ->
                        setState(DashboardBottomSheetState.GenericState(medGeneric))
                    }, onCompanyDetailsRequested = { company ->
                        setState(DashboardBottomSheetState.CompanyState(company))
                    })
            }
        }


    }
    viewModel.fetchSelectedOptionData()
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    viewModel: MedicineListViewModel = hiltViewModel(),
    onMedicineDetailsRequested: (Medicine) -> Unit,
    onMedicineGenericDetailsRequested: (Generic) -> Unit,
    onCompanyDetailsRequested: (Company) -> Unit
) {
    val state by viewModel.selectedCategoryItemShowableList.observeAsState(CommonState.Idle)



    Column(modifier) {
        SearchHeader(viewModel)
        state.toComposable {
            when (it) {
                is ShowableListData.MedicineGenericShowableListData -> {
                    MedicineGenericListView(
                        it.list,
                        onMedicineGenericDetailsRequested
                    )
                }

                is ShowableListData.MedicineShowableListData -> MedicineListView(
                    medicineListState = it.list,
                    onItemClick = onMedicineDetailsRequested
                )

                is ShowableListData.CompanyShowableListData -> CompanyListViewComposable(
                    list = it.list,
                    onItemClicked = onCompanyDetailsRequested
                )
            }
        }
    }
}


@Composable
fun SearchHeader(
    viewModel: MedicineListViewModel = hiltViewModel()
) {
    var selectedCategory by remember {
        mutableStateOf(0)
    }

    val listTypes = remember {
        arrayListOf(
            "Medicine",
            "Generic",
            "Brand"
        )
    }

    val selectedItemText = listTypes[selectedCategory]

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

            val searchState by remember {
                viewModel.searchQueryState
            }
            OutlinedTextField(
                value = searchState, onValueChange = {
                    viewModel.searchMedicine(it)
                },
                label = {
                    Text("Search by $selectedItemText Name")
                }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )


            Spacer(modifier = Modifier.padding(top = 16.dp))

            FancyRadioGroup(
                options = listTypes,
                selectedItemCorner = 0.dp
            ) { index, value ->
                selectedCategory = index
                viewModel.selectCategory(index)
            }
            CommonDivider(verticalSpace = 16.dp)

            FancyRadioGroup(options = arrayListOf("A to Z", "Z to A")) { index, value ->
                viewModel.onListOrderSelected(index)
            }
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
        Column {
            DashboardPage()
        }
    }
}