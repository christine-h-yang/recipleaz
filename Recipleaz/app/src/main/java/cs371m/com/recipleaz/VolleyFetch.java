package cs371m.com.recipleaz;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Code taken from witchel.
 */

public class VolleyFetch {

    public void add(final RecipeJSON.IRecipeJSON recipeJSON, String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                recipeJSON.fetchComplete(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recipeJSON.fetchCancel();
            }
        });

        Net.getInstance().addToRequestQueue(jsonObjectRequest, MainActivity.AppName);
    }
}
