package com.tarekbohdima.bakingapp.domain.repository

import com.tarekbohdima.bakingapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun observeRecipes(): Flow<List<Recipe>>
    fun observeRecipe(id: Int): Flow<Recipe?>
    suspend fun refreshRecipes()
}
