package com.shubhobrataroy.bdmedmate.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.shubhobrataroy.bdmedmate.data.bdDb.BDMedDatabase
import com.shubhobrataroy.bdmedmate.ui.theme.BDMedMateIndianMedicineToBangladeshiMedicineTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db= Room.databaseBuilder(this@MainActivity, BDMedDatabase::class.java,"bdDb")
            .createFromAsset("medicine.db")
            .build()
        GlobalScope.launch {
           val data = db.getBDMedDao().getAllBrandData()
            val a=0
        }
        setContent {
            BDMedMateIndianMedicineToBangladeshiMedicineTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BDMedMateIndianMedicineToBangladeshiMedicineTheme {
        Greeting("Android")
    }
}