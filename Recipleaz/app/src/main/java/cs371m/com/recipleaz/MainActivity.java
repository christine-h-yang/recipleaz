package cs371m.com.recipleaz;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements RecipeJSON.IRecipeJSON {

    protected static final String AppName = "Recipeaz";

    private Button searchButton;
    private EditText searchText;
    private Net net;
    private VolleyFetch volleyFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.search_button);
        searchText = findViewById(R.id.search_text);

        net = Net.getInstance();
        Net.init(this);

        volleyFetch = new VolleyFetch();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRecipe();
            }
        });
    }

    private void searchRecipe() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.edamam.com")
                .appendPath("search")
                .appendQueryParameter("app_id", "74554488")
                .appendQueryParameter("app_key", "2bdf7a97678717c50a95741d4df27b70")
                .appendQueryParameter("q", searchText.getText().toString());
        String url = builder.build().toString();

        volleyFetch.add(this, url);
    }

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchCancel() {

    }

    @Override
    public void fetchComplete() {

    }
}
