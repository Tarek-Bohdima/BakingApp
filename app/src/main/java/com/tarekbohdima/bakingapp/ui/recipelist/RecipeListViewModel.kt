package com.tarekbohdima.bakingapp.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarekbohdima.bakingapp.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository,
) : ViewModel() {

    val uiState: StateFlow<RecipeListUiState> =
        repository.observeRecipes()
            .map<_, RecipeListUiState> { RecipeListUiState.Success(it) }
            .catch { emit(RecipeListUiState.Error(it.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RecipeListUiState.Loading,
            )

    fun refresh() {
        viewModelScope.launch {
            runCatching { repository.refreshRecipes() }
        }
    }
}
