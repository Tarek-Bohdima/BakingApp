package com.tarekbohdima.bakingapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tarekbohdima.bakingapp.data.local.entity.IngredientEntity
import com.tarekbohdima.bakingapp.data.local.entity.RecipeEntity
import com.tarekbohdima.bakingapp.data.local.entity.RecipeWithDetails
import com.tarekbohdima.bakingapp.data.local.entity.StepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun observeAllRecipes(): Flow<List<RecipeWithDetails>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun observeRecipe(id: Int): Flow<RecipeWithDetails?>

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>)

    @Transaction
    suspend fun insertFullRecipes(
        recipes: List<RecipeEntity>,
        ingredients: List<IngredientEntity>,
        steps: List<StepEntity>,
    ) {
        insertRecipes(recipes)
        insertIngredients(ingredients)
        insertSteps(steps)
    }
}
