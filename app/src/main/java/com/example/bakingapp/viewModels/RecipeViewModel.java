package com.example.bakingapp.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.repository.Repository;

import java.util.ArrayList;

public class RecipeViewModel extends ViewModel {

    private Repository repository;
    MutableLiveData<ArrayList<Recipes>> recipesList = new MutableLiveData<>();

    @ViewModelInject
    public RecipeViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ArrayList<Recipes>> getRecipesList() {
        return recipesList;
    }

}
