package com.example.bakingapp.di;

import com.example.bakingapp.network.RecipeApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bakingapp.Constants.BASE_URL;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Provides
    @Singleton
    public RecipeApiService provideRecipeApiService(GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(RecipeApiService.class);
    }

    @Provides
    @Singleton
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

}
