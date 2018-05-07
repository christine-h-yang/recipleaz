package cs371m.com.recipleaz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipeListActivity extends AppCompatActivity
        implements RecipeListAdapter.DeleteRecipeInterface {

    private RecipeListAdapter adapter;
    private ProgressBar progressBar;
    protected LinearLayoutManager recyclerViewLayoutManager;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_view);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView rv = findViewById(R.id.recipes);

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(recyclerViewLayoutManager);

        adapter = new RecipeListAdapter(this);
        adapter.setIsSavedRecipesList(true);
        adapter.setDeleteRecipeInterface(this);
        rv.setAdapter(adapter);

        loadSavedRecipes();
    }

    private void loadSavedRecipes() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            return;
        }

        String email = user.getEmail().replaceAll("\\.", "@");
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference(email);

        Query query = userDB.child("saved_recipes");
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Recipe> savedRecipes = new ArrayList<>();
                        for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                            Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                            savedRecipes.add(recipe);
                        }
                        adapter.add(savedRecipes);
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d("saved_recipes_error", "Date query cancelled");
                    }
                });
        userDB.child("saved_recipes");
    }

    public void deleteSavedRecipe(Recipe recipe) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            return;
        }

        String email = user.getEmail().replaceAll("\\.", "@");
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference(email);
        String recipeIdentifier = recipe.instructionsURL.replaceAll("[\\.\\/]", "@");

        userDB.child("saved_recipes").child(recipeIdentifier).removeValue();
    }
}
