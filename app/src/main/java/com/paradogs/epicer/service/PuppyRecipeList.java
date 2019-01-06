package com.paradogs.epicer.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PuppyRecipeList
{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("version")
    @Expose
    private double version;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("results")
    @Expose
    private List<PuppyRecipe> results = null;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public double getVersion()
    {
        return version;
    }

    public void setVersion(double version)
    {
        this.version = version;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

    public List<PuppyRecipe> getRecipes()
    {
        return results;
    }
}