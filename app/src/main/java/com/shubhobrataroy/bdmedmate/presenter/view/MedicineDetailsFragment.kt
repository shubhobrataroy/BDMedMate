package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shubhobrataroy.bdmedmate.domain.model.Company
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.presenter.viewmodel.MedicineListViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by shubhobrataroy on 21,October,2021
 **/

@ExperimentalFoundationApi
@AndroidEntryPoint
class MedicineDetailsFragment(
    private val viewModel: MedicineListViewModel,
    private val medicineEntity: Medicine,
    private val genericsEntity: MedGeneric?,
    private val company: Company?
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MedMateTheme {
                    MedicineDetails(medicineEntity, genericsEntity, company)
                }
            }
        }
    }


    @Composable
    fun MedicineItemView(
        medicine: Medicine,
        generic: MedGeneric?,
        company: Company?
    ) {

        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 6.dp),
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                item {
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
                }

                item {
                    ItemExtraData(generic, company)
                }



                item {
                    CommonTitle(title = "Similar Medicines")

                    SimilarMedicines(
                        viewModel.fetchSimilarMeds(medicine).observeAsState(null), this@LazyColumn
                    )
                }
            }
        }
    }

    @Composable
    fun ItemExtraData(
        genericsEntity: MedGeneric?,
        company: Company?
    ) {
        Column {
            CommonDivider()

            if (company != null)
                Text(text = company.name)

            if (genericsEntity != null)
                MedGenericView(medGeneric = genericsEntity)


            Spacer(
                modifier = Modifier
                    .height(24.dp),
            )

        }
    }

    @Composable
    fun MedicineDetails(
        medicineEntity: Medicine,
        genericsEntity: MedGeneric?, company: Company?
    ) {
        Card(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
            MedicineItemView(medicineEntity, genericsEntity, company)
        }
    }

    @Composable
    fun SimilarMedView(currentData: Medicine) {
        Card(
            elevation = 1.dp,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .width(IntrinsicSize.Max)
                .clickable {

                }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = currentData.name + " ${currentData.strength ?: ""}",
                fontStyle = FontStyle.Italic,
            )
        }
    }

    @Composable
    fun SimilarMedsListView(data: List<Medicine>, lazyListScope: LazyListScope) {
//        val iterationCount = 2
//        val extraData = data.size % iterationCount
//        val iteration = data.size - extraData
//
//        var i = 0

//        lazyListScope.items(data.windowed(iterationCount,step = 1))
//        { items->
//            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
//                for (j in 0 until iterationCount)
//                    SimilarMedView(currentData = items[j])
//            }
//        }

        lazyListScope.item {
            LazyRow {
                items(data)
                { item ->
                    SimilarMedView(currentData = item)
                }
            }
        }

    }

    @Composable
    fun SimilarMedicines(
        similarMeds: State<CommonState<List<Medicine>>?>,
        lazyListScope: LazyListScope
    ) {

        when (val state = similarMeds.value) {
            CommonState.Fetching -> CenterProgress()
            is CommonState.Success ->
                SimilarMedsListView(state.data, lazyListScope)
        }

    }


}