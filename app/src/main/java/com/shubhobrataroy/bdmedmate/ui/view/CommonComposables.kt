package com.shubhobrataroy.bdmedmate.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.ui.CommonState
import com.shubhobrataroy.bdmedmate.ui.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme


/**
 * Created by shubhobrataroy on 21,October,2021
 **/


@Composable
fun CommonDivider(
    verticalSpace: Dp = 8.dp,
    topSpace: Dp = verticalSpace,
    bottomSpace: Dp = verticalSpace,
    thickness: Dp = 1.dp
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(topSpace),
        )
        Divider(
            Modifier
                .height(thickness)
                .padding(horizontal = 8.dp)
        )

        Spacer(
            modifier = Modifier
                .height(bottomSpace),
        )
    }
}


@Composable
fun CommonTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = CurrentColorPalette.secondary
    )
}

@Composable
fun Paragraph(title: String, paragraph: String?) {
    if (paragraph != null)
        Column {
            CommonTitle(title = title)
            Text(text = paragraph)
            CommonDivider()
        }
}

@Composable
fun MedGenericView(
    medGeneric: MedGeneric,
    isGenericMainView: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier){
        if (!isGenericMainView)
            Text(
                text = medGeneric.name,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = CurrentColorPalette.secondary
            )
        else
            Text(
                text = medGeneric.name,
                style = MaterialTheme.typography.h5,
                color = CurrentColorPalette.secondary
            )
        CommonDivider()
        Paragraph(title = "Dosage", medGeneric.dosage)
        Paragraph(title = "Indication", medGeneric.indication)
        Paragraph(title = "Side Effect", paragraph = medGeneric.sideEffect)

    }
}

@Composable
fun <T : Any> CommonState<T>.toComposable(
    defaultValue: T? = null,
    successContent: @Composable (value: T) -> Unit
) {
    when (this) {
        CommonState.Fetching -> CenterProgress()
        is CommonState.Success -> successContent(this.data)

        else -> {
            if (defaultValue != null) successContent(defaultValue)
        }
    }
}


@Composable
fun FancyRadioGroup(
    options: List<String>,
    modifier: Modifier? = null,
    primaryColor: Color = MaterialTheme.colors.surface,
    previouslySelectedIndex: Int = 0,
    selectedColor: Color = CurrentColorPalette.primary,
    unSelectedColor: Color = primaryColor,
    containerCorners: Dp = 16.dp,
    selectedItemCorner: Dp = 16.dp,
    onItemSelected: ((index: Int, item: String) -> Unit)? = null
) {
    var selectedIndex by remember { mutableStateOf(previouslySelectedIndex) }
    Card(
        shape = RoundedCornerShape(containerCorners),
        modifier = modifier ?: Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.background(primaryColor),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEachIndexed { index, it ->

                val isSelected = selectedIndex == index
                Card(
                    shape = RoundedCornerShape(if (isSelected) selectedItemCorner else 0.dp),
                    elevation = 0.dp, modifier = Modifier
                        .weight(.98f)
                        .fillMaxWidth()
                        .clickable {
                            if (onItemSelected != null) {
                                onItemSelected(index, it)
                            }
                            selectedIndex = index

                        }
                ) {
                    Box(
                        Modifier
                            .background(if (isSelected) selectedColor else unSelectedColor)
                            .height(34.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            it,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = if (isSelected)
                                CurrentColorPalette.onSecondary
                            else CurrentColorPalette.onSurface,
                            maxLines = 1
                        )
                    }
                }
                if (index < options.lastIndex)
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .weight(.02f)
                    )
            }
        }
    }
}


@Composable
fun RegularRadioGroup(options: Array<String>, onChanged: (value: String, index: Int) -> Unit) {

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    MedMateTheme {
        FancyRadioGroup(
            options = arrayListOf("Hello", "Mello", "Test"),
            containerCorners = 0.dp, previouslySelectedIndex = 1
        )
    }
}

@Composable
fun CenterProgress() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}