package com.tarekbohdima.bakingapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

val PREF_RECIPE_ID = intPreferencesKey("selected_recipe_id")
val PREF_RECIPE_NAME = stringPreferencesKey("selected_recipe_name")
val PREF_INGREDIENTS = stringPreferencesKey("selected_ingredients")

class IngredientsWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { WidgetContent() }
    }
}

@Composable
private fun WidgetContent() {
    val context = LocalContext.current
    val prefs = currentState<Preferences>()
    val recipeName = prefs[PREF_RECIPE_NAME] ?: "Tap to select a recipe"
    val ingredientsRaw = prefs[PREF_INGREDIENTS] ?: ""
    val ingredients = if (ingredientsRaw.isBlank()) emptyList() else ingredientsRaw.split("|")

    GlanceTheme {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.background)
                .padding(12.dp)
                .clickable(actionStartActivity<WidgetConfigActivity>()),
        ) {
            Text(
                text = recipeName,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                modifier = GlanceModifier.fillMaxWidth().padding(bottom = 8.dp),
            )
            if (ingredients.isEmpty()) {
                Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No ingredients", style = TextStyle(fontSize = 12.sp))
                }
            } else {
                ingredients.forEach { line ->
                    Text(
                        text = "• $line",
                        style = TextStyle(fontSize = 12.sp),
                        modifier = GlanceModifier.fillMaxWidth().padding(vertical = 2.dp),
                    )
                }
            }
        }
    }
}
