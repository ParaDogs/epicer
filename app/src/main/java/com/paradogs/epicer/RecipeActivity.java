package com.paradogs.epicer;

import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.paradogs.epicer.model.Recipe;
import com.paradogs.epicer.utils.Constants;
import com.paradogs.epicer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity
{
    private WebView webView;
    private Menu menu;
    private Recipe recipe;
    private List<Recipe> favorites = new ArrayList<Recipe>();

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        /*ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.recipe_layout);
        constraintLayout.setBackgroundResource(R.drawable.ic_downloading_message);*/

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.setWebViewClient(new WebViewClient());
        recipe = (Recipe) getIntent().getSerializableExtra(Constants.KEY_RECIPE);

        webView.loadUrl(recipe.getDescriptionHref());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;

        favorites = Utils.deserialize(Constants.getFavoritesLocation(this));
        if (favorites.contains(recipe))
            getMenuInflater().inflate(R.menu.del_from_favorites, menu);
        else
            getMenuInflater().inflate(R.menu.add_to_favorites, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() != R.id.add_to_favorites)
        {
            menu.clear();
            getMenuInflater().inflate(R.menu.del_from_favorites, menu);
            if (!favorites.contains(recipe))
            {
                favorites.add(recipe);
                Utils.serialize(favorites, Constants.getFavoritesLocation(this));
            }
        }
        else
        {
            menu.clear();
            getMenuInflater().inflate(R.menu.add_to_favorites, menu);
            favorites.remove(recipe);
            Utils.serialize(favorites, Constants.getFavoritesLocation(this));
        }

        return super.onOptionsItemSelected(item);
    }
}
