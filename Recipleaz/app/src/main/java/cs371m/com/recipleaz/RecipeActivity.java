package cs371m.com.recipleaz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeName;
    private TextView recipeCost;
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
        ingredientList = findViewById(R.id.ingredient_list);

        recipe = getIntent().getParcelableExtra("recipe");

        setTitle(recipe.title);

        recipeName.setText(recipe.title);
        recipeCost.setText("$$$");
        setRecipeImage(recipe.imageURL);

        // Handling Ingredient List
        LinearLayoutManager recyclerViewLayoutManager =
                new LinearLayoutManager(getApplicationContext());
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
}
