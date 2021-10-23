package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
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
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.presenter.viewmodel.MedicineListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    val viewModel by viewModels<MedicineListViewModel>()

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


        val medsListViewHolder = MedsListViewHolder { medicine ->
            val medicineDetailsFragment =
                MedicineDetailsFragment.getInstance(medicine)
            medicineDetailsFragment.show(supportFragmentManager, medicine.name)
        }
        Column {
            SearchHeader(viewModel.searchQueryState)
            medsListViewHolder.MedicineListView(medicineListState = state)
        }
    }

    @Composable
    fun SearchHeader(searchQueryState: MutableState<String>) {
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

                val searchState by remember {
                    searchQueryState
                }
                OutlinedTextField(
                    value = searchState, onValueChange = {
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
                    FancyRadioGroup(arrayListOf("A to Z", "Z to A")) { index, value ->
                        viewModel.onMedicineOrderSelected(index)
                    }
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


@Composable
fun CenterProgress() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Composable
fun FancyRadioGroup(options: Array<String>, onChanged: (value: String, index: Int) -> Unit) {

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