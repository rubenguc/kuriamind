package com.kuriamind.di

import android.content.Context
import androidx.room.Room
import com.kuriamind.data.local.BlockDao
import com.kuriamind.data.local.KuriamindDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KuriamindDatabase =
        Room.databaseBuilder(
            context,
            KuriamindDatabase::class.java,
            "kuriamind.db",
        ).build()

    @Provides
    fun provideBlockDao(database: KuriamindDatabase): BlockDao = database.blockDao()
}
