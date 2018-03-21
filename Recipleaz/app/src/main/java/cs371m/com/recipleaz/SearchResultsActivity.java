package cs371m.com.recipleaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class SearchResultsActivity extends AppCompatActivity {

    private RecipeResultsAdapter adapter;
    private Net net;
    private VolleyFetch volleyFetch;
    private RecipeService recipeService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        net = Net.getInstance();
        volleyFetch = new VolleyFetch();
        recipeService = new RecipeService(net, volleyFetch);

        // TODO: can add progress bar indicator

        RecyclerView rv = findViewById(R.id.search_results);
        adapter = new RecipeResultsAdapter(getApplicationContext());
        rv.setAdapter(adapter);

        Intent intent = getIntent();
        String searchText = intent.getStringExtra("searchText");
        recipeService.searchRecipe(searchText);
    }
}
