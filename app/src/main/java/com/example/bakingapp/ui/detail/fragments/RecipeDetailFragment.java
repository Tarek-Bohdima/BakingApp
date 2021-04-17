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

package com.example.bakingapp.ui.detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.adapters.IngredientsAdapter;
import com.example.bakingapp.ui.detail.adapters.StepsAdapter;
import com.example.bakingapp.ui.detail.viewmodels.SharedlViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    public static final String CURRENT_RECIPE = "current_recipe";
    private final List<Steps> stepsList = new ArrayList<>();
    private final List<Ingredients> ingredientsList = new ArrayList<>();
    private FragmentRecipeDetailBinding fragmentRecipeDetailBinding;
    private SharedlViewModel sharedlViewModel;
    private boolean isTwoPane;
    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private Recipes currentRecipe;
    private List<Steps> stepsData;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance() {
        return new RecipeDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentRecipeDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail,
                container, false);

        return fragmentRecipeDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isTwoPane = getResources().getBoolean(R.bool.isTablet);
        setupViewModel();
        setupIngredientsRecyclerView();
        ingredientsAdapter.setIngredientsData(currentRecipe.getIngredients());
        setupStepsRecyclerView();
        stepsAdapter.setStepsData(stepsData);
    }

    private void setupViewModel() {
        sharedlViewModel = new ViewModelProvider(requireActivity()).get(SharedlViewModel.class);
        currentRecipe = sharedlViewModel.getCurrentRecipe();
        stepsData = sharedlViewModel.getCurrentRecipe().getSteps();
    }

    private void setupStepsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stepsAdapter = new StepsAdapter(stepsList, this);
        RecyclerView stepsRecyclerView = fragmentRecipeDetailBinding.stepsRecyclerview;
        stepsRecyclerView.setNestedScrollingEnabled(false);
        stepsRecyclerView.setLayoutManager(layoutManager);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    private void setupIngredientsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredientsAdapter = new IngredientsAdapter(ingredientsList);
        RecyclerView ingredientsRecyclerView = fragmentRecipeDetailBinding.ingredientsRecyclerview;
        ingredientsRecyclerView.setNestedScrollingEnabled(false);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
    }

    @Override
    public void onStepClick(Steps steps) {
        sharedlViewModel.getCurrentStep().setValue(steps);

        if (!isTwoPane) {
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.item_detail_container, stepDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}