package com.shubhobrataroy.bdmedmate.presenter.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhobrataroy.bdmedmate.domain.model.MedGeneric
import com.shubhobrataroy.bdmedmate.presenter.CommonState
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.CurrentColorPalette
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.MedMateTheme

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
fun <T : Any> CommonState<T>.toComposable(successContent: @Composable (value:T) -> Unit) {
    when(this)
    {
        CommonState.Fetching -> CenterProgress()
        is CommonState.Success -> successContent(this.data)
        else -> {}
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    MedMateTheme {
        MedGenericView(medGeneric = MedGeneric("Azythomycin", "Hello"))
    }
}