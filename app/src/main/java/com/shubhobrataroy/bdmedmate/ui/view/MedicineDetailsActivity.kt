package com.shubhobrataroy.bdmedmate.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhobrataroy.bdmedmate.domain.model.Generic
import com.shubhobrataroy.bdmedmate.domain.model.Medicine
import com.shubhobrataroy.bdmedmate.ui.ui.theme.MedMateTheme

class MedicineDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedMateTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting2("Android")
                }
            }
        }
    }
}

@Composable
fun MedicineDetails(medicineEntity: Medicine, genericsEntity: Generic) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = medicineEntity.name,
            fontWeight = FontWeight.Bold
        )


        Card(
            elevation = 3.dp,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = genericsEntity.name,
                fontStyle = FontStyle.Italic,
            )
        }
        if (medicineEntity.type != null)
            Text(
                text = medicineEntity.type,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )

        if (medicineEntity.strength != null)
            Text(text = medicineEntity.strength)


    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    MedMateTheme {
        MedicineDetails(
            Medicine(
                "ABC Medicine", "Syrup","250 mg",
            ),
            Generic("Azythomycin")
        )
    }
}