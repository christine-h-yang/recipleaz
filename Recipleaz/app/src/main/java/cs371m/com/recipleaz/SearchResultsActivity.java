package cs371m.com.recipleaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecipeResultsAdapter adapter;
    private RecipeService recipeService;
    protected LinearLayoutManager recyclerViewLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        recipeService = RecipeService.getInstance();

        // TODO: can add progress bar indicator

        RecyclerView rv = findViewById(R.id.search_results);

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(recyclerViewLayoutManager);
        //rv.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecipeResultsAdapter(getApplicationContext());
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerViewTouchListener(this, rv,
                        new RecyclerViewTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                        intent.putExtra("searchText", "beef");
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

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
                        Recipe recipe = new Recipe();
                        recipe.imageURL = result.getString("image");
                        recipe.title = result.getString("label");
                        recipes.add(recipe);
                        Log.d("testing", "fetchComplete: " + recipe);
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "fetchComplete: " + e.toString());
                }

                adapter.add(recipes);
            }

            @Override
            public void fetchCancel() {

            }
        });
    }
}
