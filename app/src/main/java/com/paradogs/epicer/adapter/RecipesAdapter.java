package com.paradogs.epicer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.paradogs.epicer.R;
import com.paradogs.epicer.RecipeActivity;
import com.paradogs.epicer.model.Recipe;
import com.paradogs.epicer.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>
{
    private Context context;
    private List<Recipe> recipesList = new ArrayList<>();

    public RecipesAdapter(Context context)
    {
        this.context = context;
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mRecipeImage;
        private TextView mTitle;
        private TextView mDescriptionHref;
        private TextView mIngredientsView;

        private Recipe recipe;

        RecipesViewHolder(View itemView)
        {
            super(itemView);
            mRecipeImage = (ImageView) itemView.findViewById(R.id.recipeImageView);
            mTitle = (TextView) itemView.findViewById(R.id.titleView);
            mDescriptionHref = (TextView) itemView.findViewById(R.id.descriptionView);
            mIngredientsView = (TextView) itemView.findViewById(R.id.ingredientsView);

            itemView.setOnClickListener(e ->
            {
                if (recipe == null) return;

                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra(Constants.KEY_RECIPE, recipe);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }

        void bind(Recipe recipe)
        {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.ic_menu_camera)
                    .circleCrop();

            String recipeThumbnailURL = recipe.getThumbnail();

            Glide.with(itemView.getContext())
                    .load(recipeThumbnailURL)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(mRecipeImage);

            mTitle.setText(recipe.getTitle());
            mDescriptionHref.setText(recipe.getDescriptionHref());
            StringBuilder ingredients = new StringBuilder();
            for (String ingredient : recipe.getIngredients())
                ingredients.append(ingredient).append(" ");
            mIngredientsView.setText(ingredients);
        }

        public void setRecipe(Recipe recipe)
        {
            this.recipe = recipe;
        }
    }

    public void setItems(Collection<Recipe> recipes)
    {
        recipesList.addAll(recipes);
        notifyDataSetChanged();
    }

    public void clearItems()
    {
        recipesList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recipe_info, viewGroup, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder recipesViewHolder, int i)
    {
        recipesViewHolder.setRecipe(recipesList.get(i));
        recipesViewHolder.bind(recipesList.get(i));
    }

    @Override
    public int getItemCount()
    {
        return recipesList.size();
    }
}