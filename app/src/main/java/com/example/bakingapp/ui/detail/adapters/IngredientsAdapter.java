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

package com.example.bakingapp.ui.detail.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.ItemIngredientBinding;
import com.example.bakingapp.model.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    ItemIngredientBinding itemIngredientBinding;
    List<Ingredients> ingredients;

    public IngredientsAdapter(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemIngredientBinding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IngredientsViewHolder(itemIngredientBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredients currentIngredients = ingredients.get(position);
        holder.bind(currentIngredients);
    }

    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    public void setIngredientsData(List<Ingredients> ingredientsData) {
        ingredients = ingredientsData;
        notifyDataSetChanged();
    }

    public static class IngredientsViewHolder extends RecyclerView.ViewHolder {

        ItemIngredientBinding itemIngredientBinding;

        public IngredientsViewHolder(ItemIngredientBinding itemIngredientBinding) {
            super(itemIngredientBinding.getRoot());
            this.itemIngredientBinding = itemIngredientBinding;
        }

        public void bind(Ingredients ingredients) {
            itemIngredientBinding.setIngredients(ingredients);

            itemIngredientBinding.executePendingBindings();
        }
    }
}
