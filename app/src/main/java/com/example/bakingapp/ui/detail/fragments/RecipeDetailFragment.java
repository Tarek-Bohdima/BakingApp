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

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.adapters.IngredientsAdapter;
import com.example.bakingapp.ui.detail.adapters.StepsAdapter;
import com.example.bakingapp.ui.detail.viewmodels.RecipeDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    public static final String CURRENT_RECIPE = "current_recipe";
    private Recipes currentRecipe;
    private final List<Steps> stepsList = new ArrayList<>();
    private FragmentRecipeDetailBinding fragmentRecipeDetailBinding;
    private RecipeDetailViewModel mViewModel;
    private boolean mTwoPane;
    private StepsAdapter stepsAdapter;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance() {
        return new RecipeDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        fragmentRecipeDetailBinding = DataBindingUtil
                .inflate(inflater,
                        R.layout.fragment_recipe_detail,
                        container,false);


        return fragmentRecipeDetailBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTwoPane = getResources().getBoolean(R.bool.isTablet);
        mViewModel = new ViewModelProvider(requireActivity()).get(RecipeDetailViewModel.class);
        currentRecipe = mViewModel.getCurrentRecipe();
        List<Steps> stepsData = mViewModel.getCurrentRecipe().getSteps();


        setupIngredientsRecyclerView();

        setupStepsRecyclerView();
        stepsAdapter.setStepsData(stepsData);

    }

    private void setupStepsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        stepsAdapter = new StepsAdapter(stepsList, this);

        RecyclerView stepsRecyclerView = fragmentRecipeDetailBinding.stepsRecyclerview;
        stepsRecyclerView.setLayoutManager(layoutManager);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    private void setupIngredientsRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.setIngredientsData(currentRecipe.getIngredients());

        RecyclerView ingredientsRecyclerView = fragmentRecipeDetailBinding.ingredientsRecyclerview;
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    }

    @Override
    public void onStepClick(Steps steps) {

        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance();
        mViewModel.setCurrentStep(steps);
        String uri = steps.getVideoURL();
        Timber.tag(Constants.TAG).d("RecipeDetailFragment: onStepClick() called with: video uri = [" + uri + "]");

        if (mTwoPane) {

        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.item_detail_container, stepDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}