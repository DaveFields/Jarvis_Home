package com.example.jarvishome.core.dimodules

import android.content.Context
import com.example.jarvishome.core.common.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): Preferences {
        return Preferences(context)
    }
}