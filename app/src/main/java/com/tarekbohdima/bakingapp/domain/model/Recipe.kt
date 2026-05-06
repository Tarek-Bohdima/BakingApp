package com.tarekbohdima.bakingapp.domain.model

data class Recipe(
    val id: Int,
    val name: String,
    val servings: Int,
    val image: String,
    val ingredients: List<Ingredient>,
    val steps: List<Step>,
)
