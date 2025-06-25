package com.iameben.keysmith.di

import android.content.Context
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.util.PasswordGenerator
import com.iameben.keysmith.util.WordListUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): AppPreferences = AppPreferences(context)

    @Provides
    @Singleton
    fun provideWordListUtil(@ApplicationContext context: Context): WordListUtil = WordListUtil

    @Provides
    @Singleton
    fun providePasswordGenerator(preferences: AppPreferences, wordListUtil: WordListUtil): PasswordGenerator =
        PasswordGenerator(preferences, wordListUtil)
}