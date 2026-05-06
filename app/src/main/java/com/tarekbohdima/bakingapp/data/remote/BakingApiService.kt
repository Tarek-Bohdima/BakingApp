package com.tarekbohdima.bakingapp.data.remote

import com.tarekbohdima.bakingapp.data.remote.dto.RecipeDto
import retrofit2.http.GET

interface BakingApiService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    suspend fun getRecipes(): List<RecipeDto>
}
