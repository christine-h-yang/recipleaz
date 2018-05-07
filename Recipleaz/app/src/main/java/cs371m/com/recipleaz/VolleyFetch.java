package cs371m.com.recipleaz;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;


public class VolleyFetch implements Response.Listener<JSONObject>, Response.ErrorListener {

    public void request(String url) {
        final VolleyFetch parent = this;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parent.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parent.onErrorResponse(error);
            }
        });

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Net.getInstance().addToRequestQueue(jsonObjectRequest, MainActivity.AppName);
    }

    @Override
    public void onResponse(JSONObject response) {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }
}
