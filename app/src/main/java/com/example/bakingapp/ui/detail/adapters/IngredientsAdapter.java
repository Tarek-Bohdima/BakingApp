package com.example.bakingapp.ui.detail.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.IngredientItemBinding;
import com.example.bakingapp.model.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    IngredientItemBinding ingredientItemBinding;
    List<Ingredients> ingredients;

    public IngredientsAdapter(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ingredientItemBinding = IngredientItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IngredientsViewHolder(ingredientItemBinding);
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

    public class IngredientsViewHolder extends RecyclerView.ViewHolder{

        IngredientItemBinding ingredientItemBinding;

        public IngredientsViewHolder(IngredientItemBinding ingredientItemBinding) {
            super(ingredientItemBinding.getRoot());
            this.ingredientItemBinding = ingredientItemBinding;
        }

        public void bind(Ingredients ingredients) {
            ingredientItemBinding.quantityTextview.setText(String.valueOf(ingredients.getQuantity()));
            ingredientItemBinding.measurementUnitTextview.setText(ingredients.getMeasure());
            ingredientItemBinding.ingredientsTextview.setText(ingredients.getIngredient());

            ingredientItemBinding.executePendingBindings();
        }
    }
}
