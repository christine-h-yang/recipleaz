package cs371m.com.recipleaz;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeName;
    private TextView recipeLink;
    private TextView priceEstimate;
    private TextView recipeYield;
    private RecyclerView ingredientList;
    private Recipe recipe;
    private Button saveRecipeButton;

    private IngredientsAdapter ingredientsAdapter;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

        mAuth = FirebaseAuth.getInstance();

        recipeImage = findViewById(R.id.recipe_pic);
        recipeName = findViewById(R.id.recipe_name);
        recipeLink = findViewById(R.id.recipe_link);
        priceEstimate = findViewById(R.id.price_estimate);
        recipeYield = findViewById(R.id.recipe_yield);
        ingredientList = findViewById(R.id.ingredient_list);
        saveRecipeButton = findViewById(R.id.save_recipe_button);

        recipe = getIntent().getParcelableExtra("recipe");

        setTitle(recipe.title);

        recipeName.setText(recipe.title);
        recipeYield.setText(getString(R.string.servings, recipe.yield));
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

        ingredientsAdapter = new IngredientsAdapter(getApplicationContext());
        ingredientList.setAdapter(ingredientsAdapter);
        ingredientsAdapter.add(recipe.ingredients, recipe.ingredientsChecklist);

        saveRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update ingredient checklist
                recipe.ingredientsChecklist = ingredientsAdapter.getChecklist();

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {
                    Toast.makeText(RecipeActivity.this, "Please login to use this feature",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = user.getEmail().replaceAll("\\.", "@");
                DatabaseReference userDB = FirebaseDatabase.getInstance().getReference(email);
                String recipeIdentifier = recipe.instructionsURL.replaceAll("[\\.\\/]", "@");

                userDB.child("saved_recipes").child(recipeIdentifier).setValue(recipe);
                Toast.makeText(RecipeActivity.this, "Recipe saved!", Toast.LENGTH_LONG).show();
            }
        });


        RecipleazBackendService.getInstance().getPriceEstimate(recipe.title, new RecipeJSON.IRecipeJSON() {
            @Override
            public void fetchStart() {

            }

            @Override
            public void fetchComplete(JSONObject response) {
                try {
                    String estimate = response.getString("estimate");
                    Log.d("DONE", "fetchComplete: " + estimate);
                    priceEstimate.setText("Price Estimate: " + estimate);
                } catch (JSONException e) {
                    Log.d("DONE", "fetchComplete: " + e.getMessage());
                }

            }

            @Override
            public void fetchCancel() {
                Log.d("CANCLLED", "fetchComplete: cancelled");
            }
        });
    }

    private void setRecipeImage(String url) {
        Glide.with(getApplicationContext())
                .load(url)
                .into(recipeImage);
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
