package com.example.bakingapp.ui.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.databinding.ActivityItemListBinding;
import com.example.bakingapp.ui.detail.ItemDetailActivity;
import com.example.bakingapp.ui.list.adapters.SimpleRecyclerViewAdapter;
import com.example.bakingapp.ui.list.viewmodels.RecipeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
public class RecipesActivity extends AppCompatActivity {


    private ActivityItemListBinding activityItemListBinding;
    private RecipeViewModel recipeViewModel;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityItemListBinding = DataBindingUtil.setContentView(this, R.layout.activity_item_list );
        activityItemListBinding.setLifecycleOwner(this);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(activityItemListBinding.toolbar);
        activityItemListBinding.toolbar.setTitle(getTitle());

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        mTwoPane = findViewById(R.id.item_detail_container) != null;

        RecyclerView recyclerView = activityItemListBinding.includedLayout.itemList;
        setupRecyclerView(recyclerView);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recipeViewModel.getRecipesList().observe(this,
                recipes -> recyclerView.setAdapter(new SimpleRecyclerViewAdapter(RecipesActivity.this, recipes, mTwoPane)));
    }


}