package com.example.bakingapp.network;

import com.example.bakingapp.model.Recipes;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface RecipeApiService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Single<List<Recipes>> getRecipes();
    
}
