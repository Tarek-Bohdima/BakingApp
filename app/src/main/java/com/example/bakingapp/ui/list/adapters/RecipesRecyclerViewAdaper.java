package com.example.bakingapp.ui.list.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.ui.detail.RecipeDetailActivity;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class RecipesRecyclerViewAdaper
        extends RecyclerView.Adapter<RecipesRecyclerViewAdaper.ViewHolder> {

    private final List<Recipes> recipes;
    private final View.OnClickListener mOnClickListener = RecipesRecyclerViewAdaper::onClick;

    public RecipesRecyclerViewAdaper(List<Recipes> recipes) {
        this.recipes = recipes;
    }

    private static void onClick(View v) {
        Recipes currentRecipe = (Recipes) v.getTag();
        Context context = v.getContext();
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailFragment.CURRENT_RECIPE, currentRecipe);
        context.startActivity(intent);
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(recipes.get(position).getName());
        Timber.tag(Constants.TAG).d(String.format(Locale.ENGLISH,
                "aRecipesRecyclerViewAdaper: onBindViewHolder() called with: recipe's name = [%s]"
                , recipes.get(position).getName()));

        holder.itemView.setTag(recipes.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }



    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.recipe_name);
        }
    }
}
