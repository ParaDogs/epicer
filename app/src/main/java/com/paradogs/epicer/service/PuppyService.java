package com.paradogs.epicer.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paradogs.epicer.model.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.support.constraint.Constraints.TAG;

interface PuppyAPI
{
    @GET("?q=0")
    Call<PuppyRecipeList> search(
            @Query("q") String query,
            @Query("p") int page,
            @Query("i") String ingredients);
}

public class PuppyService
{
    private static PuppyAPI puppyAPI;
    private Retrofit retrofit;

    public PuppyService()
    {
        final String BASE_URL = "http://www.recipepuppy.com/api/";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        puppyAPI = retrofit.create(PuppyAPI.class);
    }

    public List<Recipe> getRecipes(int page, List<String> ingredients) throws IOException
    {
        final List<Recipe> recipes = new ArrayList<Recipe>();
        String tmp = "";
        for(String ingredient : ingredients)
        {
            tmp += (ingredient +", ");
        }

        Response<PuppyRecipeList> recipeListResponse = puppyAPI.search(ingredients.get(0),page, tmp).execute();

        if(recipeListResponse.isSuccessful())
        {
            assert recipeListResponse.body() != null;
            for(PuppyRecipe pr : recipeListResponse.body().getRecipes())
            {
                Recipe recipe = new Recipe(pr.getTitle(), Arrays.asList(pr.getIngredients().split(", ")), pr.getThumbnail(), pr.getHref());
                recipes.add(recipe);
            }
        }

        return recipes;
    }
}