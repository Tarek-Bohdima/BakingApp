package com.example.bakingapp.ui.list.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.ui.detail.RecipeDetailActivity;
import com.example.bakingapp.ui.detail.fragments.ItemDetailFragment;
import com.example.bakingapp.ui.list.RecipesActivity;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class SimpleRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder> {

    private final RecipesActivity mParentActivity;
    private final List<Recipes> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = this::isTablet;

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
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, currentRecipe);
            Timber.tag(Constants.TAG).d(String.format(Locale.ENGLISH,"SimpleItemRecyclerViewAdapter: isNotTablet() called with: recipe = [%s]"
                    , currentRecipe.getName()));

            context.startActivity(intent);
        }
    }

    public SimpleRecyclerViewAdapter(RecipesActivity parent,
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
