package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ListData
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.presenter.viewmodel.MedicineListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    val viewModel by viewModels<MedicineListViewModel>()

    private val listTypes = arrayListOf(
        "Medicine",
        "Generic",
        "Brand"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedMateTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AllDataView(viewModel = viewModel)
                }
            }
        }

        viewModel.fetchMedicineList()
    }

    @Composable
    fun AllDataView(viewModel: MedicineListViewModel) {
        val state by viewModel.selectedCategoryItemList.observeAsState(CommonState.Idle)



        Column {
            SearchHeader(viewModel.searchQueryState)
            state.toComposable {
                when (it) {
                    is ListData.MedicineGenericListData -> {
                        MedGenericsViewHolder().MedicineGenericListView(
                            it.list
                        ){

                        }
                    }
                    is ListData.MedicineListData -> MedicineListViewer(listData = it)
                }
            }
        }
    }

    @Composable
    fun MedicineListViewer(listData: ListData.MedicineListData) {
        val medsListViewHolder = MedsListViewHolder { medicine ->
            val medicineDetailsFragment =
                MedicineDetailsFragment.getInstance(medicine)
            medicineDetailsFragment.show(supportFragmentManager, medicine.name)
        }
        medsListViewHolder.MedicineListView(medicineListState = listData.list)
    }

    @Composable
    fun SearchHeader(searchQueryState: MutableState<String>) {
        val selectedCategory by remember {
            mutableStateOf(0)
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

                Text(text = selectedItemText, Modifier.fillMaxWidth())

                val searchState by remember {
                    searchQueryState
                }
                OutlinedTextField(
                    value = searchState, onValueChange = {
                        viewModel.searchMedicine(it)
                    },
                    label = {
                        Text("Search by $selectedItemText Name")
                    }, modifier = Modifier.fillMaxWidth(.95f)
                )


                CommonDivider(16.dp)

                FancyRadioGroup(
                    options = listTypes, containerCorners = 0.dp,
                    selectedItemCorner = 0.dp
                ) { index, value ->
                        viewModel.selectCategory(index)
                }
                CommonDivider(verticalSpace = 16.dp)

                FancyRadioGroup(arrayListOf("A to Z", "Z to A")) { index, value ->
                    viewModel.onMedicineOrderSelected(index)
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
                SearchHeader(mutableStateOf(""))
//                MedsListViewHolder().MedicineListView(medicineListState = CommonState.Success(meds))
//           } SearchHeader()
            }
        }
    }
}


