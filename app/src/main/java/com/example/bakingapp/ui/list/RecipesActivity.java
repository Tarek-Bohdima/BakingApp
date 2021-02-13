package com.example.bakingapp.ui.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.ui.detail.RecipeDetailActivity;
import com.example.bakingapp.ui.list.adapters.SimpleRecyclerViewAdapter;
import com.example.bakingapp.ui.list.viewmodels.RecipeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * An activity representing a list of Items. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets.
 */
@AndroidEntryPoint
public class RecipesActivity extends AppCompatActivity {

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.bakingapp.databinding.ActivityRecipesListBinding activityItemListBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);
        activityItemListBinding.setLifecycleOwner(this);

        setSupportActionBar(activityItemListBinding.toolbar);
        activityItemListBinding.toolbar.setTitle(getTitle());

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);


        RecyclerView recyclerView = activityItemListBinding.includedLayout.itemList;
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recipeViewModel.getRecipesList().observe(this,
                recipes -> recyclerView.setAdapter(new SimpleRecyclerViewAdapter(recipes)));
    }


}