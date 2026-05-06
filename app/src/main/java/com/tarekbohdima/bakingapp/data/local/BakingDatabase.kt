package com.tarekbohdima.bakingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tarekbohdima.bakingapp.data.local.entity.IngredientEntity
import com.tarekbohdima.bakingapp.data.local.entity.RecipeEntity
import com.tarekbohdima.bakingapp.data.local.entity.StepEntity

@Database(
    entities = [RecipeEntity::class, IngredientEntity::class, StepEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class BakingDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
