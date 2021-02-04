package com.example.bakingapp.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.databinding.ActivityItemListBinding;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.ui.detail.ItemDetailActivity;
import com.example.bakingapp.ui.detail.ItemDetailFragment;

import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

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
        recipeViewModel.getRecipesList().observe(this, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(RecipesActivity.this,recipes,mTwoPane));
            }
        });
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipesActivity mParentActivity;
        private final List<Recipes> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTablet(view);
            }
        };

        private void isTablet(View view) {
            Recipes currentRecipe = (Recipes) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(ItemDetailFragment.ARG_ITEM_ID, currentRecipe);
                Timber.tag(Constants.TAG).d(String.format(Locale.ENGLISH,"SimpleItemRecyclerViewAdapter: isTablet() called with: recipe = [%s]",
                        currentRecipe.getName()));
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, currentRecipe);
                Timber.tag(Constants.TAG).d(String.format(Locale.ENGLISH,"SimpleItemRecyclerViewAdapter: isNotTablet() called with: recipe = [%s]"
                        , currentRecipe.getName()));

                context.startActivity(intent);
            }
        }

        SimpleItemRecyclerViewAdapter(RecipesActivity parent,
                                      List<Recipes> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getName());
            Timber.tag(Constants.TAG).d(String.format(Locale.ENGLISH,
                    "SimpleItemRecyclerViewAdapter: onBindViewHolder() called with: recipe's name = [%s]"
                    , mValues.get(position).getName()));

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.recipe_name);
            }
        }
    }
}