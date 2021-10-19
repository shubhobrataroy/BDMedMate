package com.shubhobrataroy.bdmedmate.presenter

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.GenericIndicationEntity
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.MedGenericsEntity
import com.shubhobrataroy.bdmedmate.data.bdDb.entity.MedicineEntity
import com.shubhobrataroy.bdmedmate.presenter.ui.theme.BDMedMateIndianMedicineToBangladeshiMedicineTheme

class MedicineDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BDMedMateIndianMedicineToBangladeshiMedicineTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting2("Android")
                }
            }
        }
    }
}

@Composable
fun MedicineDetails(medicineEntity: MedicineEntity, genericsEntity: MedGenericsEntity) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = medicineEntity.brandName ?: "",
            fontWeight = FontWeight.Bold
        )

        if (genericsEntity.genericName != null)
           Card(elevation = 3.dp,
               modifier = Modifier.padding(2.dp)
               ) {
               Text(
                   modifier = Modifier.padding(4.dp),
                   text = genericsEntity.genericName ?: "",
                   fontStyle = FontStyle.Italic,
               )
           }
        if (medicineEntity.form != null)
            Text(
                text = medicineEntity.form ?: "",
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )

        if (medicineEntity.strength != null)
            Text(text = medicineEntity.strength ?: "")


    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    BDMedMateIndianMedicineToBangladeshiMedicineTheme {
        MedicineDetails(
            MedicineEntity(
                "1", "2", "3",
                "ABC Medicine", "Syrup", "250 mg", "20.0", "250"
            ),
            MedGenericsEntity("","Azythomucin")
        )
    }
}