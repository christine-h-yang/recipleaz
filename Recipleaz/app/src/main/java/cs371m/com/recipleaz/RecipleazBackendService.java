package cs371m.com.recipleaz;

import android.net.Uri;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class RecipleazBackendService {

    // TODO: the below two fields should be read from a configuration file and not checked into
    // version control
    private final String APP_ID = "74554488";
    private final String APP_KEY = "2bdf7a97678717c50a95741d4df27b70";

    private final String API_URL = "api.edamam.com";

    private static final RecipleazBackendService instance = new RecipleazBackendService();

    //private constructor to avoid client applications to use constructor
    private RecipleazBackendService() {
    }

    public static RecipleazBackendService getInstance(){
        return instance;
    }

    // TODO: I think it would be cool to have requests use method chaining
    // define a request class with the following methods, where any 'onXXXX' returns itself:
    // onStart, onSuccess, onError, onCancel, start, and cancel methods)
    // then, API methods would be easier to write because there would be less boilerplate
    public void searchRecipe(String query, final RecipeJSON.IRecipeJSON fetchEventHandler) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(API_URL)
                .appendPath("search")
                .appendQueryParameter("app_id", APP_ID)
                .appendQueryParameter("app_key", APP_KEY)
                .appendQueryParameter("q", query);
        String url = builder.build().toString();

        VolleyFetch volleyFetch = new VolleyFetch() {
            @Override
            public void onResponse(JSONObject response) {
                fetchEventHandler.fetchComplete(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                fetchEventHandler.fetchCancel();
            }
        };
        volleyFetch.request(url);
    }
}