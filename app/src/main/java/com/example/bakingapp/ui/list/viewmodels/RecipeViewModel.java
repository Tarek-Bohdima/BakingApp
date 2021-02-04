package com.example.bakingapp.ui.list.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.repository.Repository;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    LiveData<List<Recipes>> recipesLivedata;
    private Repository repository;

    @ViewModelInject
    public RecipeViewModel(Repository repository) {
        recipesLivedata = repository.getRecipes();
        this.repository = repository;

    }

    public LiveData<List<Recipes>> getRecipesList() {
        return recipesLivedata;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clearDisposables();
    }
}
