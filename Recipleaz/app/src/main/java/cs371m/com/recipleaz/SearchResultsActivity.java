package cs371m.com.recipleaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecipeListAdapter adapter;
    private RecipeService recipeService;
    protected LinearLayoutManager recyclerViewLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_view);

        recipeService = RecipeService.getInstance();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView rv = findViewById(R.id.recipes);

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(recyclerViewLayoutManager);
        //rv.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecipeListAdapter(this);
        rv.setAdapter(adapter);

        Intent intent = getIntent();
        String searchText = intent.getStringExtra("searchText");
        recipeService.searchRecipe(searchText, new RecipeJSON.IRecipeJSON() {
            @Override
            public void fetchStart() {

            }

            @Override
            public void fetchComplete(JSONObject jsonObject) {
                Log.d("response", jsonObject.toString());
                List<Recipe> recipes = new ArrayList<>();
                try {
                    JSONArray results = jsonObject.getJSONArray("hits");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = ((JSONObject) results.get(i)).getJSONObject("recipe");

                        String imageURL = result.getString("image");
                        String title = result.getString("label");
                        int yield = result.getInt("yield");
                        String instructionsURL = result.getString("url");
                        JSONArray ingredientsJSON = result.getJSONArray("ingredientLines");
                        List<String> ingredients = new ArrayList<>();
                        List<Boolean> ingredientsCheckList = new ArrayList<>();
                        for (int j = 0; j < ingredientsJSON.length(); j++) {
                            ingredients.add(ingredientsJSON.getString(j));
                            ingredientsCheckList.add(false);
                        }

                        Recipe recipe = new Recipe(imageURL, yield, ingredients, ingredientsCheckList, instructionsURL, title);
                        recipes.add(recipe);
                        Log.d("testing", "fetchComplete: " + recipe);
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "fetchComplete: " + e.toString());
                }

                progressBar.setVisibility(View.GONE);
                adapter.add(recipes);
            }

            @Override
            public void fetchCancel() {

            }
        });
    }
}
