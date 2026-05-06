package com.tarekbohdima.bakingapp.ui.recipedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tarekbohdima.bakingapp.domain.model.Ingredient
import com.tarekbohdima.bakingapp.domain.model.Recipe
import com.tarekbohdima.bakingapp.domain.model.Step
import com.tarekbohdima.bakingapp.ui.stepplayer.StepPlayerContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    onStepClick: (recipeId: Int, stepIndex: Int) -> Unit,
    onBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (uiState as? RecipeDetailUiState.Success)?.recipe?.name ?: ""
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val state = uiState) {
                is RecipeDetailUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is RecipeDetailUiState.NotFound -> Text(
                    "Recipe not found.",
                    modifier = Modifier.align(Alignment.Center),
                )
                is RecipeDetailUiState.Success -> AdaptiveDetailLayout(
                    recipe = state.recipe,
                    onStepClick = onStepClick,
                )
            }
        }
    }
}

@Composable
private fun AdaptiveDetailLayout(
    recipe: Recipe,
    onStepClick: (recipeId: Int, stepIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        if (maxWidth >= 600.dp) {
            var selectedStepIndex by remember { mutableIntStateOf(0) }
            Row(Modifier.fillMaxSize()) {
                DetailPane(
                    recipe = recipe,
                    selectedStepIndex = selectedStepIndex,
                    onStepClick = { index -> selectedStepIndex = index },
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(),
                )
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                )
                StepPlayerContent(
                    step = recipe.steps.getOrNull(selectedStepIndex),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        } else {
            DetailPane(
                recipe = recipe,
                selectedStepIndex = -1,
                onStepClick = { index -> onStepClick(recipe.id, index) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun DetailPane(
    recipe: Recipe,
    selectedStepIndex: Int,
    onStepClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Text(
                "Ingredients",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )
        }
        items(recipe.ingredients.size) { i ->
            IngredientRow(recipe.ingredients[i])
        }
        item {
            Text(
                "Steps",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )
        }
        itemsIndexed(recipe.steps) { index, step ->
            StepRow(
                step = step,
                isSelected = index == selectedStepIndex,
                onClick = { onStepClick(index) },
            )
        }
    }
}

@Composable
private fun IngredientRow(ingredient: Ingredient) {
    ListItem(
        headlineContent = { Text(ingredient.ingredient) },
        trailingContent = {
            Text(
                "${ingredient.quantity.toDisplayString()} ${ingredient.measure}",
                style = MaterialTheme.typography.labelMedium,
            )
        },
    )
    Divider()
}

@Composable
private fun StepRow(step: Step, isSelected: Boolean, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(step.shortDescription) },
        leadingContent = {
            Text(
                "${step.id + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.fillMaxWidth() else Modifier,
            ),
        tonalElevation = if (isSelected) 8.dp else 0.dp,
    )
    Divider()
}

private fun Double.toDisplayString(): String = if (this == kotlin.math.floor(this)) toLong().toString() else toString()
