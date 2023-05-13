package com.shubhobrataroy.bdmedmate

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shubhobrataroy.bdmedmate.data.bd.BDMedDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BDDatabaseTester {

    private lateinit var db: BDMedDatabase

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.databaseBuilder(context, BDMedDatabase::class.java, "bdDb")
            .createFromAsset("medicine.db")
            .build()
    }


    @Test
    fun testPresetDataForMedicines() {
        val allBrandData = db.getBDMedDao().getAllBrandData()
        assert(allBrandData.isNotEmpty()) {
            "Brand data is empty"
        }
    }

    @Test
    fun testPresetDataForGenerics() {
        val allGenericData =
            db.getBDMedDao().getAllMedGenericsData(SimpleSQLiteQuery("select * from generic"))
        assert(allGenericData.isNotEmpty()) {
            "Generic data is empty"
        }
    }

    @Test
    fun testPresetDataForCompanyNames() {
        val allCompanyData = db.getBDMedDao().getAllCompanyData()
        assert(allCompanyData.isNotEmpty()) {
            "Company data is empty"
        }
    }

    @Test
    fun testPresetDataForIndications() {
        val indicationsData = db.getBDMedDao().getAllIndications()
        assert(indicationsData.isNotEmpty()) {
            "Indications data is empty"
        }
    }


    @Test
    fun testAllMedicineDetailsData() {
        runBlocking {
            val detailedData = db.getAllMedicines()
            assert(detailedData.isNotEmpty()) {
                "Medicine data is empty"
            }
        }
    }

    @Test
    fun getMedByGenerics() {
        runBlocking {
            val generics = db.getBDMedDao().getGenericById("1")
            assert(generics != null) { "Generic Not found" }
            Log.d("GenMeds","Generics $generics")
            val meds = db.getBDMedDao().getSimilarMedicine(generics.firstOrNull()?.genericId ?: "").firstOrNull()
            Log.d("GenMeds",meds.toString())
            assert(meds.isNullOrEmpty().not())
        }
    }

}