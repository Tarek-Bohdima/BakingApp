package com.example.bakingapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.example.bakingapp.R;
import com.example.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.adapters.StepsAdapter;
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
public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.OnStepClickListener {

    public ActivityRecipeDetailBinding activityRecipeDetailBinding;

    private RecipeDetailViewModel recipeDetailViewModel;

    private boolean mTwoPane;

    private Recipes currentRecipe;

    private StepDetailFragment stepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRecipeDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        activityRecipeDetailBinding.setLifecycleOwner(this);

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
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, recipeDetailFragment)
                    .commit();

            if (mTwoPane) {

                stepDetailFragment = StepDetailFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_steps_container, stepDetailFragment)
                        .commit();
            }
        }
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
            navigateUpTo(new Intent(this, RecipesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepClick(Steps steps) {
        stepDetailFragment = StepDetailFragment.newInstance();
        if (mTwoPane) {

        }else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container,stepDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}