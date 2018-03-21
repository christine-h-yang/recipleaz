package cs371m.com.recipleaz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
