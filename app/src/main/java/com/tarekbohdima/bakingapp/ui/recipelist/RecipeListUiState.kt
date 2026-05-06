package com.tarekbohdima.bakingapp.ui.recipelist

import com.tarekbohdima.bakingapp.domain.model.Recipe

sealed interface RecipeListUiState {
    data object Loading : RecipeListUiState
    data class Success(val recipes: List<Recipe>) : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}
