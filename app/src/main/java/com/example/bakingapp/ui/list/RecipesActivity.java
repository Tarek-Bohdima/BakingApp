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

package com.example.bakingapp.ui.list;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.IdlingResource.EspressoIdlingResource;
import com.example.bakingapp.R;
import com.example.bakingapp.databinding.ActivityRecipesListBinding;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.repository.Preferences;
import com.example.bakingapp.ui.detail.RecipeDetailActivity;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;
import com.example.bakingapp.ui.list.adapters.RecipesAdapter;
import com.example.bakingapp.ui.list.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * An activity representing a list of Items. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets.
 */
public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeClickListener {

    @Inject
    Preferences preferences;
    private final List<Recipes> recipesList = new ArrayList<>();
    RecipesAdapter recipesAdapter;
    private ActivityRecipesListBinding activityItemListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityItemListBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);
        activityItemListBinding.setLifecycleOwner(this);
        setSupportActionBar(activityItemListBinding.toolbar);
        activityItemListBinding.toolbar.setTitle(getTitle());
        RecipeViewModel recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        setupRecyclerView();
        recipeViewModel.getRecipesList().observe(this,
                recipesData -> {
                    recipesAdapter.setData(recipesData);
                    EspressoIdlingResource.decrement();
                });
    }

    private void setupRecyclerView() {
        EspressoIdlingResource.increment();
        recipesAdapter = new RecipesAdapter(this, recipesList);
        RecyclerView recyclerView = activityItemListBinding.includedLayout.itemList;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipesAdapter);
    }

    @Override
    public void onRecipeClick(Recipes recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailFragment.CURRENT_RECIPE, recipe);
        startActivity(intent);
    }
}