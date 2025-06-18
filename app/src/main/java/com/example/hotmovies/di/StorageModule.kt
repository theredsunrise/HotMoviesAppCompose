package com.example.hotmovies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.hotmovies.R
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.infrastructure.storage.Crypto
import com.example.hotmovies.infrastructure.storage.CryptoInterface
import com.example.hotmovies.infrastructure.storage.SecureRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Singleton
    @Binds
    abstract fun bindsCrypto(impl: Crypto): CryptoInterface

    @Singleton
    @Binds
    abstract fun bindsSecureRepository(impl: SecureRepository): SecureRepositoryInterface

    companion object {
        @Singleton
        @Provides
        fun providesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = { appContext.preferencesDataStoreFile(appContext.getString(R.string.secure_datastore_file)) }
            )
        }
    }
}