package com.example.bakingapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bakingapp.Constants;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.network.RecipeApiService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


public class Repository {

    private final RecipeApiService recipeApiService;
    private final MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public Repository(RecipeApiService recipeApiService) {
        this.recipeApiService = recipeApiService;
    }


    public LiveData<List<Recipes>> getRecipes() {
        @NonNull Single<List<Recipes>> recipesListSingle = recipeApiService.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable.add(recipesListSingle
                .subscribe(recipes::postValue,
                        e -> Timber.tag(Constants.TAG).d("Repository: getRecipes() called with: error = [" + e.getMessage() + "]")));
        return recipes;
    }

    public void clearDisposables() {
        // Using clear will clear all, but can accept new disposable
        compositeDisposable.clear();
    }
}
