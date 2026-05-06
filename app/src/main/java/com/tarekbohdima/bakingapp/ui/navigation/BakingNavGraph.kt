package com.tarekbohdima.bakingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tarekbohdima.bakingapp.ui.recipedetail.RecipeDetailScreen
import com.tarekbohdima.bakingapp.ui.recipelist.RecipeListScreen
import com.tarekbohdima.bakingapp.ui.stepplayer.StepPlayerScreen

@Composable
fun BakingNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = RecipeList) {
        composable<RecipeList> {
            RecipeListScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(RecipeDetail(recipeId))
                },
            )
        }
        composable<RecipeDetail> {
            RecipeDetailScreen(
                onStepClick = { recipeId, stepIndex ->
                    navController.navigate(StepPlayer(recipeId, stepIndex))
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable<StepPlayer> {
            StepPlayerScreen(onBack = { navController.popBackStack() })
        }
    }
}
