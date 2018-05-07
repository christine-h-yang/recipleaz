package cs371m.com.recipleaz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String AppName = "Recipleaz";
    protected static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
    protected static final int PICK_FROM_GALLERY = 1;

    private ActionBarDrawerToggle toggle;
    private Button searchButton;
    private EditText searchText;
    private ImageButton uploadPhoto;
    private FirebaseAuth mAuth;
    private Menu drawerMenu;

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updateUserDisplay();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerMenu = navigationView.getMenu();

        searchButton = findViewById(R.id.search_button);
        searchText = findViewById(R.id.search_text);
        uploadPhoto = findViewById(R.id.photo_upload);

        Net.init(this);
        new ClarifaiService().init();

        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event)
            {
                if(EditorInfo.IME_ACTION_DONE==actionId || EditorInfo.IME_ACTION_UNSPECIFIED==actionId) {
                    search(searchText.getText().toString());
                    return true;
                }

                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(searchText.getText().toString());
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGalleryPermissions();
            }
        });
    }

    private void search(String text) {
        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
        intent.putExtra("searchText", text);
        MainActivity.this.startActivity(intent);
    }

    private void uploadPhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
    }

    private void checkGalleryPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        } else {
            Log.d("WAT", "checkGalleryPermissions: uploading a photo");
            uploadPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadPhoto();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            byte[] img = ClarifaiService.retrieveSelectedImage(this, data);
            ClarifaiService.getInstance().processImage(img, new ClarifaiService.ProcessClarifaiConcepts() {
                @Override
                public void onPostExecute(List<String> concepts) {
                    search(TextUtils.join(" ", concepts));
                }
            });
        }
    }

    private void updateUserDisplay() {
        String loginString = "";
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loginString = String.format("Log out as %s", user.getEmail());
        } else {
            loginString = "Login";
        }
        MenuItem logMenu = drawerMenu.findItem(R.id.nav_login);
        if (logMenu != null) {
            logMenu.setTitle(loginString);
            logMenu.setTitleCondensed(loginString);
        }
    }

    private void login() {
        LoginAndSignupDialogFragment dialogFragment = new LoginAndSignupDialogFragment();
        dialogFragment.setLoginAndSignupInterface(new LoginAndSignupDialogFragment.LoginAndSignupInterface() {
            @Override
            public void signup(String email, String password) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    updateUserDisplay();
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

            @Override
            public void login(String email, String password) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    updateUserDisplay();
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        dialogFragment.show(getFragmentManager(), "login");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_login:
                if (item.getTitle().toString().equals(getString(R.string.login))) {
                    login();
                } else {
                    mAuth.signOut();
                    updateUserDisplay();
                }
                return true;
            case R.id.saved_recipes:
                Intent intent = new Intent(getApplicationContext(), SavedRecipeListActivity.class);
                MainActivity.this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserDisplay();
    }
}
