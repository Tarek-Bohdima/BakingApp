package com.example.bakingapp.ui.detail.viewmodels;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.Constants;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class RecipeDetailViewModel extends ViewModel {

    private final Recipes currentRecipe;
    private Steps currentStep;

    @Inject
    public RecipeDetailViewModel(SavedStateHandle stateHandle) {
        this.currentRecipe = stateHandle.get(RecipeDetailFragment.CURRENT_RECIPE);
    }

    public Recipes getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentStep(Steps steps) {

        currentStep = steps;
        Timber.tag(Constants.TAG).d("RecipeDetailViewModel: setCurrentStep() called with: steps = [" + steps + "]");
    }

    public Steps getCurrentStep() {
        return currentStep;
    }
}