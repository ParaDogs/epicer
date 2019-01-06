package com.paradogs.epicer;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.paradogs.epicer.adapter.RecipesAdapter;
import com.paradogs.epicer.model.Recipe;
import com.paradogs.epicer.service.PuppyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchResultActivity extends AppCompatActivity
{
    List<String> ingredients = new ArrayList<String>();
    int page;

    ProgressBar progressBar;

    LinearLayoutManager manager;
    private RecyclerView recipesRecyclerView;
    private RecipesAdapter recipesAdapter;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    ConstraintLayout layout;

    private void initRecyclerView()
    {
        page = 1;
        recipesRecyclerView = findViewById(R.id.recipes_list);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar);
        manager = new LinearLayoutManager(this);
        recipesRecyclerView.setLayoutManager(manager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);
        recipesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recipesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems-1))
                    progressBar.setVisibility(View.VISIBLE);

                if (isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    page++;

                    try {loadRecipes();}
                    catch (ExecutionException e) {e.printStackTrace();}
                    catch (InterruptedException e) {e.printStackTrace();}
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initRecyclerView();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        layout = (ConstraintLayout) findViewById(R.id.search_layout);

        SearchView searchView = findViewById(R.id.searchView);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchView.clearFocus();

                String[] ings = query.split("[,;]");
                ingredients = new ArrayList<String>();
                recipesAdapter.clearItems();
                ingredients.addAll(Arrays.asList(ings));

                try
                {
                    if (loadRecipes())
                        layout.setBackgroundResource(0);
                    else
                        layout.setBackgroundResource(R.drawable.ic_cant_find_message);
                }
                catch (InterruptedException e){e.printStackTrace();}
                catch (ExecutionException e){e.printStackTrace();}

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //useless
                return true;
            }
        });


        layout.setBackgroundResource(R.drawable.ic_lets_search_message);
    }

    private boolean loadRecipes() throws ExecutionException, InterruptedException
    {
        DownloadRecipeTask task = new DownloadRecipeTask();
        List<Recipe> recipeList = task.execute().get();
        recipesAdapter.setItems(recipeList);
        return recipeList.size() != 0;
    }

    private class DownloadRecipeTask extends AsyncTask<Void, Void, List<Recipe>>
    {
        @Override
        protected List<Recipe> doInBackground(Void... voids)
        {
            PuppyService puppyService = new PuppyService();
            List<Recipe> recipesList = new ArrayList<Recipe>();
            try {recipesList = puppyService.getRecipes(page, ingredients);}
            catch (IOException e){e.printStackTrace();}
            return recipesList;
        }
    }
}
