package com.shubhobrataroy.bdmedmate.data.bd

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shubhobrataroy.bdmedmate.domain.MedDataSource
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by shubhobrataroy on 20,October,2021
 */
@RunWith(AndroidJUnit4::class)
class BDMedDatabaseTest : TestCase("BDDataSourceTest") {
    private lateinit var db: MedDataSource

    private val commonEmptyListMsg = "List is empty"

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.databaseBuilder(context, BDMedDatabase::class.java, "bdDb")
            .createFromAsset("medicine.db")
            .build()
    }

    @Test
    fun testGetAllMedicines() {
        runBlocking {
            db.getAllMedicines().let {
                assert(it.isNotEmpty()) {
                    commonEmptyListMsg
                }
            }
        }
    }

    @Test
    fun testGetAllGenerics() {
        runBlocking {
            db.getAllGenerics().let {
                assert(it.isNotEmpty()) {
                    commonEmptyListMsg
                }
            }
        }
    }

    @Test
    fun testGetAllCompany() {

        runBlocking {
            db.getAllCompany().let {
                assert(it.isNotEmpty()) {
                    commonEmptyListMsg
                }
            }
        }
    }

    @Test
    fun testAllForeignValuesInMedicine() {
        runBlocking {
            val missingCompanyForMeds = ArrayList<String>()
            val missingSimilarMedForMeds = ArrayList<String>()
            val missingGenericsForMeds = ArrayList<String>()

            db.getAllMedicines().forEachIndexed { index, it ->


                if (it.companyDetails() != null) {
                    Log.d(
                        name, "Missing company Details.\n" +
                                "Index:$index\nMedicine:$it"
                    )
                    missingCompanyForMeds.add(it.name)
                }
                if (it.similarMedicines().isNotEmpty()) {
                    Log.d(name, "Missing Similar Medicines.\nIndex:$index\nMedicine:$it")
                    missingSimilarMedForMeds.add(it.name)
                }
                if (it.genericFetcher() != null) {
                    Log.d(
                        name, "Missing Generics Details.\n" +
                                "Index:$index\nMedicine:$it"
                    )
                    missingGenericsForMeds.add(it.name)
                }

                Log.e(
                    name,
                    "Missing Companies For:" + missingCompanyForMeds.joinToString(",") +"\n"+
                            "Missing Generics For:" + missingGenericsForMeds.joinToString(",") +"\n"+
                            "Missing Similar For:" + missingSimilarMedForMeds.joinToString(",")+"\n"
                )
                assert(missingCompanyForMeds.isNullOrEmpty())
                assert(missingGenericsForMeds.isNullOrEmpty())
                assert(missingSimilarMedForMeds.isNullOrEmpty())
            }
        }
    }
}