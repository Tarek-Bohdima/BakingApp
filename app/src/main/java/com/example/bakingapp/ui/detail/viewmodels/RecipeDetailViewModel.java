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

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.Constants;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.fragments.RecipeDetailFragment;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class RecipeDetailViewModel extends ViewModel {

    private final Recipes currentRecipe;
    private Steps currentStep;

    @Inject
    public RecipeDetailViewModel(SavedStateHandle stateHandle) {
        this.currentRecipe = stateHandle.get(RecipeDetailFragment.CURRENT_RECIPE);
    }

    public Recipes getCurrentRecipe() {
        return currentRecipe;
    }

    public Steps getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Steps steps) {

        currentStep = steps;
        Timber.tag(Constants.TAG).d("RecipeDetailViewModel: setCurrentStep() called with: step video url = [" + steps.getVideoURL() + "]");
    }
}