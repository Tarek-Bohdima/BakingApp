package com.tarekbohdima.bakingapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.tarekbohdima.bakingapp.domain.model.Ingredient
import com.tarekbohdima.bakingapp.domain.model.Recipe
import com.tarekbohdima.bakingapp.domain.model.Step

data class RecipeWithDetails(
    @Embedded val recipe: RecipeEntity,
    @Relation(parentColumn = "id", entityColumn = "recipeId")
    val ingredients: List<IngredientEntity>,
    @Relation(parentColumn = "id", entityColumn = "recipeId")
    val steps: List<StepEntity>,
)

fun RecipeWithDetails.toDomain() = Recipe(
    id = recipe.id,
    name = recipe.name,
    servings = recipe.servings,
    image = recipe.image,
    ingredients = ingredients.map {
        Ingredient(quantity = it.quantity, measure = it.measure, ingredient = it.ingredient)
    },
    steps = steps.sortedBy { it.id }.map {
        Step(
            id = it.id,
            shortDescription = it.shortDescription,
            description = it.description,
            videoURL = it.videoURL,
            thumbnailURL = it.thumbnailURL,
        )
    },
)
