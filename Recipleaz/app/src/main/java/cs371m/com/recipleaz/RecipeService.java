package cs371m.com.recipleaz;

import android.net.Uri;


public class RecipeService implements RecipeJSON.IRecipeJSON {

    // TODO: the below two fields should be read from a configuration file and not checked into
    // version control
    private final String APP_ID = "74554488";
    private final String APP_KEY = "2bdf7a97678717c50a95741d4df27b70";

    private final String API_URL = "api.edamam.com";

    private Net net;
    private VolleyFetch volleyFetch;

    public RecipeService(Net net, VolleyFetch volleyFetch) {
        this.net = net;
        this.volleyFetch = volleyFetch;
    }

    public void searchRecipe(String query) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(API_URL)
                .appendPath("search")
                .appendQueryParameter("app_id", APP_ID)
                .appendQueryParameter("app_key", APP_KEY)
                .appendQueryParameter("q", query);
        String url = builder.build().toString();

        volleyFetch.add(this, url);
    }

    @Override
    public void fetchStart() {

    }

    @Override
    public void fetchComplete() {

    }

    @Override
    public void fetchCancel() {

    }
}
