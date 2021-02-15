package com.example.bakingapp.ui.detail.viewmodels;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.Constants;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class RecipeDetailViewModel extends ViewModel {

    private SavedStateHandle stateHandle;
    private Recipes currentRecipe;

    @Inject
    public RecipeDetailViewModel(SavedStateHandle stateHandle) {
        this.stateHandle = stateHandle;
        this.currentRecipe = stateHandle.get(RecipeDetailFragment.CURRENT_RECIPE);
        Timber.tag(Constants.TAG).d(String.format("RecipeDetailViewModel: RecipeDetailViewModel() called with: currentRecipe = [%s]", currentRecipe));
    }


    public Recipes getCurrentRecipe() {
        return currentRecipe;
    }
}