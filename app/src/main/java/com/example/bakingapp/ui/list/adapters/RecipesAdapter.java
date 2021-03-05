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

package com.example.bakingapp.ui.list.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.ItemRecipeBinding;
import com.example.bakingapp.model.Recipes;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipesAdapter
        extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    OnRecipeClickListener onRecipeClickListener;
    private List<Recipes> recipesList;

    public RecipesAdapter(OnRecipeClickListener onRecipeClickListener, List<Recipes> recipesList) {
        this.onRecipeClickListener = onRecipeClickListener;
        this.recipesList = recipesList;
    }

    public void setData(List<Recipes> recipesData) {
        recipesList = recipesData;
        notifyDataSetChanged();
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemRecipeBinding itemRecipeBinding = ItemRecipeBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemRecipeBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipes currentRecipe = recipesList.get(position);
        holder.bind(currentRecipe, onRecipeClickListener);
    }

    @Override
    public int getItemCount() {
        return recipesList == null ? 0 : recipesList.size();
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipes recipe);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecipeBinding itemRecipeBinding;

        ViewHolder(ItemRecipeBinding itemRecipeBinding) {
            super(itemRecipeBinding.getRoot());
            this.itemRecipeBinding = itemRecipeBinding;
        }

        public void bind(Recipes recipe, OnRecipeClickListener onRecipeClickListener) {
            itemRecipeBinding.setRecipe(recipe);
            itemRecipeBinding.getRoot().setOnClickListener(v -> onRecipeClickListener.onRecipeClick(recipe));
            itemRecipeBinding.executePendingBindings();
        }
    }
}
