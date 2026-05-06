package com.tarekbohdima.bakingapp.di

import android.content.Context
import androidx.room.Room
import com.tarekbohdima.bakingapp.data.local.BakingDatabase
import com.tarekbohdima.bakingapp.data.local.RecipeDao
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
    fun provideDatabase(@ApplicationContext context: Context): BakingDatabase =
        Room.databaseBuilder(context, BakingDatabase::class.java, "baking.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideRecipeDao(db: BakingDatabase): RecipeDao = db.recipeDao()
}
