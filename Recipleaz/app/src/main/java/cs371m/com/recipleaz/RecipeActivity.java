package cs371m.com.recipleaz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RecipeActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeName;
    private TextView recipeCost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

        recipeImage = findViewById(R.id.recipe_pic);
        recipeName = findViewById(R.id.recipe_name);
        recipeCost = findViewById(R.id.recipe_cost);

        recipeCost.setText("$$$");
        recipeName.setText("Example Recipe Name");
        setRecipeImage();

        setTitle("Example Recipe Name");
    }

    private void setRecipeImage() {
        String tempURL = "https://www.edamam.com/web-img/1ad/1ada7332c4b265716789ca06a0d178c8.jpg";
        Glide.with(getApplicationContext())
                .load(tempURL)
                .into(recipeImage);
    }
}
