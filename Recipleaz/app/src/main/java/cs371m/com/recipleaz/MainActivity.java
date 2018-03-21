package cs371m.com.recipleaz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    protected static final String AppName = "Recipleaz";

    private Button searchButton;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.search_button);
        searchText = findViewById(R.id.search_text);

        Net.init(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                intent.putExtra("searchText", searchText.getText().toString());
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
