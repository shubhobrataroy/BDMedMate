package com.shubhobrataroy.bdmedmate.presenter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
class MedicineDetailsFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MedicineListViewModel>()

    private val bottomSheetBehavior = object : BottomSheetBehavior.BottomSheetCallback() {
        private var lastState = -1

        override fun onStateChanged(bottomSheet: View, newState: Int) {

            if ((lastState == BottomSheetBehavior.STATE_EXPANDED &&
                        newState == BottomSheetBehavior.STATE_DRAGGING) ||
                listState?.firstVisibleItemIndex ?: -1 != 0
            ) behavior.state = BottomSheetBehavior.STATE_EXPANDED


            lastState = newState
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

    }


    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                MedMateTheme {
                    MedicineDetails(medicineEntity)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.addBottomSheetCallback(bottomSheetBehavior)
    }

    companion object {
        lateinit var medicineEntity: Medicine

        private var INSTANCE: MedicineDetailsFragment? = null

        fun getInstance(medicine: Medicine): MedicineDetailsFragment {
            medicineEntity = medicine

            return if (INSTANCE != null) INSTANCE!!
            else MedicineDetailsFragment()

        }
    }


    private var listState: LazyListState? = null

    @Composable
    fun MedicineItemView(
        medicine: Medicine,
    ) {

        listState = rememberLazyListState()
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 6.dp),

            ) {
            LazyColumn(
                state = listState!!,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                item {
                    Row {
                        Text(text = medicine.name, style = MaterialTheme.typography.h4)
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
                    ItemExtraData(
                        viewModel.getGenericsAndCompanyDetails(medicineEntity)
                            .observeAsState(initial = null)
                    )
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
        state: State<CommonState<Pair<MedGeneric?, Company?>>?>
    ) {

        state.value?.toComposable {
            val (genericsEntity, company) = it

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
    }

    @Composable
    fun MedicineDetails(
        medicineEntity: Medicine
    ) {
        Card(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
            MedicineItemView(medicineEntity)
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
                    SimilarMedView(
                        currentData = item,
                        this@MedicineDetailsFragment.childFragmentManager
                    )
                }
            }
        }

    }

    @Composable
    fun SimilarMedicines(
        similarMeds: State<CommonState<List<Medicine>>?>,
        lazyListScope: LazyListScope
    ) {

        similarMeds.value?.toComposable {
            SimilarMedsListView(it, lazyListScope)
        }

    }


}

@ExperimentalFoundationApi
@Composable
fun SimilarMedView(currentData: Medicine, fragmentManager: FragmentManager) {
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CurrentColorPalette.primary),
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .width(IntrinsicSize.Max)
            .clickable {
                MedicineDetailsFragment
                    .getInstance(currentData)
                    .show(fragmentManager, currentData.name)
            }
    ) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = currentData.name + " ${currentData.strength ?: ""}",
                fontStyle = FontStyle.Italic,
            )
        }
    }
}