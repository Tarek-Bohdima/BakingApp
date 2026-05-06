package com.tarekbohdima.bakingapp.ui.stepplayer

import com.tarekbohdima.bakingapp.domain.model.Step

sealed interface StepPlayerUiState {
    data object Loading : StepPlayerUiState
    data class Success(
        val steps: List<Step>,
        val currentIndex: Int,
    ) : StepPlayerUiState {
        val currentStep: Step get() = steps[currentIndex]
        val hasPrevious: Boolean get() = currentIndex > 0
        val hasNext: Boolean get() = currentIndex < steps.lastIndex
    }
}
