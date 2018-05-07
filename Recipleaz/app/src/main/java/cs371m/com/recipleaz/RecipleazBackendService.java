package cs371m.com.recipleaz;

import android.net.Uri;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class RecipleazBackendService {

    private final String API_URL = "http://ec2-52-32-229-1.us-west-2.compute.amazonaws.com:5000";

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
    public void getPriceEstimate(String query, final RecipeJSON.IRecipeJSON fetchEventHandler) {
        String url = API_URL + "?q=" + query;

        VolleyFetch volleyFetch = new VolleyFetch() {
            @Override
            public void onResponse(JSONObject response) {
                fetchEventHandler.fetchComplete(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR", "onErrorResponse: " + error.toString());
                fetchEventHandler.fetchCancel();
            }
        };
        volleyFetch.request(url);
    }
}