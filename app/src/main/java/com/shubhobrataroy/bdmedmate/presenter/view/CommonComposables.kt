package com.shubhobrataroy.bdmedmate.presenter.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme
import com.shubhobrataroy.bdmedmate.ui.theme.LighterGray

/**
 * Created by shubhobrataroy on 21,October,2021
 **/


@Composable
fun CommonDivider() {
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
fun MedGenericView(medGeneric: MedGeneric) {
    Column {
        Text(
            text = medGeneric.name,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = CurrentColorPalette.secondary
        )
        CommonDivider()
        Paragraph(title = "Dosage", medGeneric.dosage)
        Paragraph(title = "Indication", medGeneric.indication)
        Paragraph(title = "Side Effect", paragraph = medGeneric.sideEffect)

    }
}

@Composable
fun <T : Any> CommonState<T>.toComposable(successContent: @Composable (value: T) -> Unit) {
    when (this) {
        CommonState.Fetching -> CenterProgress()
        is CommonState.Success -> successContent(this.data)
        else -> {
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
    onItemSelected: ((index: Int, item: String) -> Unit)? = null
) {

    var selectedIndex by remember { mutableStateOf(previouslySelectedIndex) }


    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier ?: Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.background(primaryColor),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEachIndexed { index, it ->

                val isSelected = selectedIndex == index
                Card(
                    shape = RoundedCornerShape(if (isSelected) 16.dp else 0.dp),
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    MedMateTheme {
        FancyRadioGroup(options = arrayListOf("Hello","Mello"))
    }
}