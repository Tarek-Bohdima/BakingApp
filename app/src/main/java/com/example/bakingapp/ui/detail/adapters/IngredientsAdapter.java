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
