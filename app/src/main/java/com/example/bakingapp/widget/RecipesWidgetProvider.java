/*
 * MIT License
 * Copyright (c) 2021.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This project was submitted by Tarek Bohdima as part of the Android Developer
 * Nanodegree At Udacity.
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 * I, the author of the project, allow you to check the code as a reference, but if you
 * submit it, it's your own responsibility if you get expelled.
 */

package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.repository.Preferences;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipesWidgetConfigureActivity RecipesWidgetConfigureActivity}
 */
@AndroidEntryPoint
public class RecipesWidgetProvider extends AppWidgetProvider {

    public static final String INGREDIENTS_LIST = "INGREDIENTS_LIST";
    @Inject
    Preferences preferences;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, Recipes currentRecipe,
                                ArrayList<Ingredients> ingredientsArrayList) {

//        Intent intent = new Intent(context, RecipeDetailActivity.class);
//        intent.putExtra(RecipeDetailFragment.CURRENT_RECIPE, currentRecipe);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                intent, 0);

//        ArrayList<Ingredients> ingredientsList = currentRecipe.getIngredients();

        Intent serviceIntent = new Intent(context, RecipeWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        serviceIntent.putParcelableArrayListExtra(INGREDIENTS_LIST, ingredientsArrayList);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipes);
        views.setTextViewText(R.id.appwidget_text, recipeName);
        views.setRemoteAdapter(R.id.widget_list_view,serviceIntent);
        views.setEmptyView(R.id.widget_list_view,R.id.empty_text_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            String recipeName = preferences.getPrefsRecipeTitle(appWidgetId);
            Recipes currentRecipe = preferences.getcurrentRecipe(recipeName);
            ArrayList<Ingredients> ingredientsArrayList = currentRecipe.getIngredients();

            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, currentRecipe, ingredientsArrayList);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            preferences.deleteTitlePrefs(appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}