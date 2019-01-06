package com.paradogs.epicer;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.paradogs.epicer.adapter.RecipesAdapter;
import com.paradogs.epicer.model.Recipe;
import com.paradogs.epicer.utils.Constants;
import com.paradogs.epicer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity
{
    private LinearLayoutManager manager;
    private RecyclerView recipesRecyclerView;
    private RecipesAdapter recipesAdapter;
    private Menu menu;
    private ConstraintLayout layout;

    private List<Recipe> favoritesList = new ArrayList<Recipe>();

    private void initRecyclerView()
    {
        recipesRecyclerView = findViewById(R.id.favorites_list);
        layout = (ConstraintLayout) findViewById(R.id.favorites_layout);
        manager = new LinearLayoutManager(this);
        recipesRecyclerView.setLayoutManager(manager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);
        recipesAdapter.setItems(favoritesList);
        recipesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.clear_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.clear_favorites)
        {
            favoritesList = new ArrayList<Recipe>();
            File file = new File(Constants.getFavoritesLocation(this));
            file.delete();
            if(file.exists()){
                try {file.getCanonicalFile().delete();}
                catch (IOException e) {e.printStackTrace();}
                if(file.exists())
                    getApplicationContext().deleteFile(file.getName());
            }
            this.onResume();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateFavorites();
    }

    private void updateFavorites()
    {
        recipesAdapter.clearItems();
        if (loadRecipes())
            layout.setBackgroundResource(0);
        else
            layout.setBackgroundResource(R.drawable.ic_no_favorites_message);
        Log.d(null, favoritesList.toString());
    }

    private boolean loadRecipes()
    {
        favoritesList = Utils.deserialize(Constants.getFavoritesLocation(this));
        recipesAdapter.setItems(favoritesList);
        return favoritesList.size() != 0;
    }
}
