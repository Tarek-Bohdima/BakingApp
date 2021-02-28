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

package com.example.bakingapp.ui.detail.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RecipeDetailViewModel extends ViewModel {

    private final Recipes currentRecipe;
    private final List<Steps> stepsList;
    MutableLiveData<Integer> currentStepPosition;

    @Inject
    public RecipeDetailViewModel(SavedStateHandle stateHandle) {
        this.currentRecipe = stateHandle.get(RecipeDetailFragment.CURRENT_RECIPE);
        stepsList = currentRecipe.getSteps();
        currentStepPosition = new MutableLiveData<>();
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public LiveData<Integer> getCurrentStepPosition() {
        return this.currentStepPosition;
    }

    public void setCurrentStepPosition(int currentStepPosition) {
        this.currentStepPosition.setValue(currentStepPosition);
    }

    public void nextStep() {
        setCurrentStepPosition(getCurrentStepPosition().getValue() + 1);
    }

    public void previousStep() {
        setCurrentStepPosition(getCurrentStepPosition().getValue() - 1);
    }

    public Recipes getCurrentRecipe() {
        return currentRecipe;
    }

    public boolean hasNext() {
        return getCurrentStepPosition().getValue() < (stepsList.size() - 1);
    }

    public boolean hasPrevious() {
        return getCurrentStepPosition().getValue() > 0;
    }
}