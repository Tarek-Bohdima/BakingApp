package com.tarekbohdima.bakingapp.ui.recipedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tarekbohdima.bakingapp.domain.repository.RecipeRepository
import com.tarekbohdima.bakingapp.ui.navigation.RecipeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: RecipeRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<RecipeDetail>()

    val uiState: StateFlow<RecipeDetailUiState> =
        repository.observeRecipe(route.recipeId)
            .map { recipe ->
                if (recipe != null) RecipeDetailUiState.Success(recipe)
                else RecipeDetailUiState.NotFound
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RecipeDetailUiState.Loading,
            )
}
