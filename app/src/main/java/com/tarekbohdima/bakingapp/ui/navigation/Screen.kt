package com.tarekbohdima.bakingapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object RecipeList

@Serializable
data class RecipeDetail(val recipeId: Int)

@Serializable
data class StepPlayer(val recipeId: Int, val stepIndex: Int)
