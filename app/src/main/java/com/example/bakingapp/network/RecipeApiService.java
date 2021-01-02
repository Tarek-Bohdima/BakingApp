package com.example.bakingapp.network;

import com.example.bakingapp.model.Recipes;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface RecipeApiService {

    @GET
    Single<Recipes> getRecipes();
    
}
