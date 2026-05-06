package com.tarekbohdima.bakingapp.data.repository

import com.tarekbohdima.bakingapp.data.local.RecipeDao
import com.tarekbohdima.bakingapp.data.local.entity.IngredientEntity
import com.tarekbohdima.bakingapp.data.local.entity.RecipeEntity
import com.tarekbohdima.bakingapp.data.local.entity.StepEntity
import com.tarekbohdima.bakingapp.data.local.entity.toDomain
import com.tarekbohdima.bakingapp.data.remote.BakingApiService
import com.tarekbohdima.bakingapp.data.remote.dto.toEntities
import com.tarekbohdima.bakingapp.domain.model.Recipe
import com.tarekbohdima.bakingapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val api: BakingApiService,
    private val dao: RecipeDao,
) : RecipeRepository {

    override fun observeRecipes(): Flow<List<Recipe>> =
        dao.observeAllRecipes()
            .onStart { if (dao.count() == 0) refreshRecipes() }
            .map { list -> list.map { it.toDomain() } }

    override fun observeRecipe(id: Int): Flow<Recipe?> =
        dao.observeRecipe(id).map { it?.toDomain() }

    override suspend fun refreshRecipes() {
        val recipes = mutableListOf<RecipeEntity>()
        val ingredients = mutableListOf<IngredientEntity>()
        val steps = mutableListOf<StepEntity>()
        api.getRecipes().forEach { dto ->
            val (r, i, s) = dto.toEntities()
            recipes += r
            ingredients += i
            steps += s
        }
        dao.insertFullRecipes(recipes, ingredients, steps)
    }
}
