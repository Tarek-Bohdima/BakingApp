package com.tarekbohdima.bakingapp.ui.recipedetail

import com.tarekbohdima.bakingapp.domain.model.Recipe

sealed interface RecipeDetailUiState {
    data object Loading : RecipeDetailUiState
    data class Success(val recipe: Recipe) : RecipeDetailUiState
    data object NotFound : RecipeDetailUiState
}
