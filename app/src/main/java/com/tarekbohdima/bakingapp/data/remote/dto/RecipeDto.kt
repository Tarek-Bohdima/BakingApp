package com.tarekbohdima.bakingapp.data.remote.dto

import com.tarekbohdima.bakingapp.data.local.entity.IngredientEntity
import com.tarekbohdima.bakingapp.data.local.entity.RecipeEntity
import com.tarekbohdima.bakingapp.data.local.entity.StepEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val name: String,
    val servings: Int,
    val image: String = "",
    val ingredients: List<IngredientDto> = emptyList(),
    val steps: List<StepDto> = emptyList(),
)

@Serializable
data class IngredientDto(val quantity: Double, val measure: String, val ingredient: String)

@Serializable
data class StepDto(
    val id: Int,
    val shortDescription: String,
    val description: String,
    @SerialName("videoURL") val videoURL: String = "",
    @SerialName("thumbnailURL") val thumbnailURL: String = "",
)

fun RecipeDto.toEntities(): Triple<RecipeEntity, List<IngredientEntity>, List<StepEntity>> {
    val recipe = RecipeEntity(id = id, name = name, servings = servings, image = image)
    val ingredientEntities = ingredients.map {
        IngredientEntity(recipeId = id, quantity = it.quantity, measure = it.measure, ingredient = it.ingredient)
    }
    val stepEntities = steps.map {
        StepEntity(
            recipeId = id,
            id = it.id,
            shortDescription = it.shortDescription,
            description = it.description,
            videoURL = it.videoURL,
            thumbnailURL = it.thumbnailURL,
        )
    }
    return Triple(recipe, ingredientEntities, stepEntities)
}
