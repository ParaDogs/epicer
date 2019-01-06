package com.paradogs.epicer.model;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable
{
    private String title;
    private List<String> ingredients;
    private String thumbnail;
    private String descriptionHref;

    public Recipe(String title, List<String> ingredients, String thumbnail, String descriptionHref)
    {
        this.title = title;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
        if (descriptionHref.contains("http://www.kraftfoods.com"))
        {
            String tmp = "https://www.kraftrecipes.com/recipe/";
            StringBuilder num = new StringBuilder(descriptionHref.substring(descriptionHref.lastIndexOf("-") + 1, descriptionHref.length() - 5));
            while (num.length() != 6)
            {
                num.insert(0, "0");
            }
            tmp +=  num;
            tmp += descriptionHref.substring(descriptionHref.lastIndexOf("/"), descriptionHref.lastIndexOf("-"));
            descriptionHref = tmp;
        }
        this.descriptionHref = descriptionHref;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescriptionHref() {
        return descriptionHref;
    }

    @Override
    public String toString()
    {
        return title +" " +ingredients +" " +thumbnail +" " +descriptionHref +"\n";
    }

    @Override
    public boolean equals(Object obj)
    {
        return toString().equals(obj.toString());
    }
}