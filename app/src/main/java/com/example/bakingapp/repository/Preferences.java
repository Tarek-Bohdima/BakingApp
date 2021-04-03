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

package com.example.bakingapp.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bakingapp.Constants;
import com.example.bakingapp.model.Recipes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import timber.log.Timber;


public class Preferences {
    public static final String PREFS_RECIPES = "com.example.bakingapp.repository.Preferences.recipes";
    public static final String PREFS_NAME = "com.example.bakingapp.Preferences";
    public static final String PREFS_PREFIX_KEY = "appwidget_";
    public static final String PREFERENCES_CURRENT_RECIPE = "com.example.bakingapp.repository.preferences.currentRecipe";
    private final SharedPreferences sharedPreferences;
    private final List<Recipes> recipesList;
    @Inject
    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        recipesList = new ArrayList<>();
    }

//    public static void setRecipesArrayList(List<Recipes> recipesArrayList) {
//        Preferences.recipesArrayList = new ArrayList<>(recipesArrayList);
//    }

    public void saveRecipesToPreferences(List<Recipes> recipesList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();


        Set<String> recipesSet = new HashSet<>();
        for (Recipes recipe : recipesList) {
            recipesSet.add(recipe.getName());
            this.recipesList.add(recipe);
        }

        editor.putStringSet(PREFS_RECIPES,recipesSet);
        editor.apply();
    }

    public void saveCurrentRecipe(String recipeName) {
        Timber.tag(Constants.TAG).d("Preferences: saveCurrentRecipe() called with: recipeList size = [" + recipesList.size()+ "]");
        for (int i = 0; i < recipesList.size(); i++) {
            Recipes recipe = recipesList.get(i);
            if (recipe.getName().equals(recipeName)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(recipe);
                editor.putString(PREFERENCES_CURRENT_RECIPE,json);
                editor.apply();
            }
        }
    }

    public Recipes getCurrentRecipe(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREFERENCES_CURRENT_RECIPE, "");
        Recipes currentRecipe = gson.fromJson(json,Recipes.class);
        return  currentRecipe;
    }

    public List<String> getRecipesFromPreferences() {
        Set<String> recipeNamesSet = sharedPreferences.getStringSet(PREFS_RECIPES, null);
        List<String> recipeNamesList = new ArrayList<>(recipeNamesSet);

        Timber.tag(Constants.TAG).d("Preferences: getRecipesFromPreferences() called with preference: %s", recipeNamesList.get(0));
        return recipeNamesList;
    }

    public void saveRecipeTitle(int appWidgetId, String title){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_PREFIX_KEY + appWidgetId, title);
        editor.apply();
    }

    public String getPrefsRecipeTitle(int appWdgetId){
        String recipeTitle = sharedPreferences.getString(PREFS_PREFIX_KEY + appWdgetId, null);
        if (recipeTitle != null) {
            return recipeTitle;
        } else {
            return "EXAMPLE";
        }
    }

    public void deleteTitlePrefs(int appWidgetId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREFS_PREFIX_KEY + appWidgetId);
        editor.apply();
    }
}
