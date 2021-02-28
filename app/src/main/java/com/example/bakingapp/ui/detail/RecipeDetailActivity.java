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

package com.example.bakingapp.ui.detail;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.bakingapp.R;
import com.example.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;
import com.example.bakingapp.ui.detail.fragments.StepDetailFragment;
import com.example.bakingapp.ui.detail.viewmodels.RecipeDetailViewModel;
import com.example.bakingapp.ui.list.RecipesActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipesActivity}.
 */
@AndroidEntryPoint
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String STEP_DETAIL_PORTRAIT_FRAGMENT = "stepDetailPortraitFragment";
    private final RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance();
    private final StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance();
    public ActivityRecipeDetailBinding activityRecipeDetailBinding;
    RecipeDetailViewModel recipeDetailViewModel;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRecipeDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        activityRecipeDetailBinding.setLifecycleOwner(this);
        recipeDetailViewModel = new ViewModelProvider(this).get(RecipeDetailViewModel.class);

        Toolbar toolbar = activityRecipeDetailBinding.detailToolbar;
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mTwoPane = getResources().getBoolean(R.bool.isTablet);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            makePortraitFragment();
            if (mTwoPane) {
                makeTabletFragment();
            }
        }
    }

    private void makeTabletFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_steps_container, stepDetailFragment)
                .commit();
    }

    private void makePortraitFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_detail_container, recipeDetailFragment, STEP_DETAIL_PORTRAIT_FRAGMENT)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}