package com.example.bakingapp.repository;

import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.network.RecipeApiService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;


public class Repository {

    private RecipeApiService recipeApiService;

    @Inject
    public Repository(RecipeApiService recipeApiService) {
        this.recipeApiService = recipeApiService;
    }

    public Single<Recipes> getRecipes(){
        return recipeApiService.getRecipes();
    }
}
