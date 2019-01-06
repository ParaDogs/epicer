package com.paradogs.epicer.utils;

import android.support.v7.app.AppCompatActivity;

public class Constants
{
    private static final String favoritesFile = "favorites.erc";
    private static final String recentlyRecupesFile = "recentlyViewed.erc";

    public static final String KEY_RECIPE = "KEY_RECIPE";

    public static String getFavoritesLocation(AppCompatActivity activity)
    {
        return activity.getFilesDir().getAbsolutePath()+"/"+favoritesFile;
    }

    public static String getRecenlyRecipesLocation(AppCompatActivity activity)
    {
        return activity.getFilesDir().getAbsolutePath()+"/"+recentlyRecupesFile;
    }
}
