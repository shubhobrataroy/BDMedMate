package com.shubhobrataroy.bdmedmate.di

import android.content.Context
import androidx.room.Room
import com.shubhobrataroy.bdmedmate.data.RepositoryImpl
import com.shubhobrataroy.bdmedmate.data.bd.BDMedDatabase
import com.shubhobrataroy.bdmedmate.domain.Country
import com.shubhobrataroy.bdmedmate.domain.MedDataSource
import com.shubhobrataroy.bdmedmate.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.HashMap
import javax.inject.Singleton

/**
 * Created by shubhobrataroy on 20,October,2021
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun getRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl

    @Provides
    fun buildDataSources(@ApplicationContext context: Context): HashMap<Country, MedDataSource> = hashMapOf(
        Country.Bangladesh to BDMedDatabase.createInstance(context)
    )
}