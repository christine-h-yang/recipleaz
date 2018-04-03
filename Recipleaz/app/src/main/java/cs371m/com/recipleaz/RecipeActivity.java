package cs371m.com.recipleaz;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeName;
    private TextView recipeCost;
    private TextView recipeLink;
    private RecyclerView ingredientList;
    private Recipe recipe;
    private ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

        recipeImage = findViewById(R.id.recipe_pic);
        recipeName = findViewById(R.id.recipe_name);
        recipeCost = findViewById(R.id.recipe_cost);
        recipeLink = findViewById(R.id.recipe_link);
        ingredientList = findViewById(R.id.ingredient_list);

        recipe = getIntent().getParcelableExtra("recipe");

        setTitle(recipe.title);

        recipeName.setText(recipe.title);
        recipeCost.setText("$$$");
        setRecipeImage(recipe.imageURL);

        recipeLink.setPaintFlags(recipeLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recipeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = recipe.instructionsURL;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        // Handling Ingredient List
        LinearLayoutManager recyclerViewLayoutManager =
                new LinearLayoutManager(getApplicationContext()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        ingredientList.setLayoutManager(recyclerViewLayoutManager);

        loadIngredientList(recipe.ingredients);

        IngredientsAdapter adapter = new IngredientsAdapter(getApplicationContext());
        ingredientList.setAdapter(adapter);
        adapter.add(ingredients);

        // TODO: Make full screen scrollable instead of Ingredient List
    }

    private void setRecipeImage(String url) {
        Glide.with(getApplicationContext())
                .load(url)
                .into(recipeImage);
    }

    private void loadIngredientList(String[] ingredientList) {
        ingredients = new ArrayList<>();

        for (String name : ingredientList) {
            ingredients.add(new Ingredient(name, false));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
