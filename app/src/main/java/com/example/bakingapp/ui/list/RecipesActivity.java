package com.example.bakingapp.ui.list;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.databinding.ActivityRecipesListBinding;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.ui.detail.RecipeDetailActivity;
import com.example.bakingapp.ui.list.adapters.RecipesAdapter;
import com.example.bakingapp.ui.list.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * An activity representing a list of Items. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets.
 */
@AndroidEntryPoint
public class RecipesActivity extends AppCompatActivity {

    RecipesAdapter recipesAdapter;
    private ActivityRecipesListBinding activityItemListBinding;
    private final List<Recipes> recipesList = new ArrayList<>();

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
                recipesData -> recipesAdapter.setData(recipesData));
    }

    private void setupRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recipesAdapter = new RecipesAdapter(recipesList);

        RecyclerView recyclerView = activityItemListBinding.includedLayout.itemList;

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipesAdapter);

    }


}