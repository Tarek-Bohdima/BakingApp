package com.tarekbohdima.bakingapp.ui.stepplayer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tarekbohdima.bakingapp.domain.repository.RecipeRepository
import com.tarekbohdima.bakingapp.ui.navigation.StepPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StepPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: RecipeRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<StepPlayer>()
    private val currentIndex = MutableStateFlow(route.stepIndex)

    val uiState: StateFlow<StepPlayerUiState> =
        combine(
            repository.observeRecipe(route.recipeId).filterNotNull().map { it.steps },
            currentIndex,
        ) { steps, index ->
            StepPlayerUiState.Success(steps = steps, currentIndex = index.coerceIn(0, steps.lastIndex))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StepPlayerUiState.Loading,
        )

    fun goToPrevious() { currentIndex.value = (currentIndex.value - 1).coerceAtLeast(0) }
    fun goToNext(stepCount: Int) { currentIndex.value = (currentIndex.value + 1).coerceAtMost(stepCount - 1) }
}
