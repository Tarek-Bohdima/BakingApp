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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Ingredients;

import java.util.List;

import static com.example.bakingapp.widget.RecipesWidgetProvider.INGREDIENTS_LIST;

public class RecipeWidgetService extends RemoteViewsService {
    public static final String CURRENT_RECIPE_INGREDIENTS = "com.example.bakingapp.widget.ingredients";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetItemFactory(this.getApplicationContext(), intent);
    }

    static class RecipeWidgetItemFactory implements RemoteViewsFactory {

        private final Context context;
        private final int recipeWidgetId;
        private final List<Ingredients> ingredientsList;

        RecipeWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            recipeWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Bundle bundle = intent.getBundleExtra("bundle");
            ingredientsList = bundle.getParcelableArrayList(INGREDIENTS_LIST);

//            ingredientsList = intent.getParcelableArrayListExtra(INGREDIENTS_LIST);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipes_item);

            String quantity = ingredientsList.get(position).getQuantity().toString();
            views.setTextViewText(R.id.quantity_textview_widget, quantity);

            String measurementUnit = ingredientsList.get(position).getMeasure();
            views.setTextViewText(R.id.measurement_unit_textview_widget, measurementUnit);

            String ingredient = ingredientsList.get(position).getIngredient();
            views.setTextViewText(R.id.ingredients_textview_widget, ingredient);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
