package com.tarekbohdima.bakingapp.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tarekbohdima.bakingapp.domain.model.Recipe
import com.tarekbohdima.bakingapp.ui.recipelist.RecipeListUiState
import com.tarekbohdima.bakingapp.ui.recipelist.RecipeListViewModel
import com.tarekbohdima.bakingapp.ui.theme.BakingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WidgetConfigActivity : ComponentActivity() {

    private val viewModel: RecipeListViewModel by viewModels()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)

        appWidgetId = intent.extras
            ?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            BakingAppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ConfigScreen(uiState = uiState, onRecipeSelected = ::onRecipeSelected)
            }
        }
    }

    private fun onRecipeSelected(recipe: Recipe) {
        val ingredientText = recipe.ingredients.joinToString("|") {
            "${it.quantity.toLong()} ${it.measure} ${it.ingredient}"
        }
        lifecycleScope.launch {
            val manager = GlanceAppWidgetManager(this@WidgetConfigActivity)
            val glanceIds = manager.getGlanceIds(IngredientsWidget::class.java)
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(this@WidgetConfigActivity, glanceId) { prefs: MutablePreferences ->
                    prefs[PREF_RECIPE_ID] = recipe.id
                    prefs[PREF_RECIPE_NAME] = recipe.name
                    prefs[PREF_INGREDIENTS] = ingredientText
                }
            }
            IngredientsWidget().updateAll(this@WidgetConfigActivity)
            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigScreen(uiState: RecipeListUiState, onRecipeSelected: (Recipe) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Select a Recipe") }) },
    ) { innerPadding ->
        when (uiState) {
            is RecipeListUiState.Loading -> Text("Loading…", modifier = Modifier.padding(innerPadding))
            is RecipeListUiState.Error -> Text(uiState.message, modifier = Modifier.padding(innerPadding))
            is RecipeListUiState.Success -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) {
                items(uiState.recipes, key = { it.id }) { recipe ->
                    ListItem(
                        headlineContent = { Text(recipe.name) },
                        supportingContent = { Text("Serves ${recipe.servings}", style = MaterialTheme.typography.labelMedium) },
                        modifier = Modifier.clickable { onRecipeSelected(recipe) },
                    )
                }
            }
        }
    }
}
