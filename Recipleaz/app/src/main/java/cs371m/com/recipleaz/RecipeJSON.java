package cs371m.com.recipleaz;

import org.json.JSONObject;

public class RecipeJSON {
    // The interface that must be implemented to complete the RedditJSON
    // lifecycle properly.  The code you write in there will somehow turn
    // what you get from Reddit into a list of RedditRecords
    public interface IRecipeJSON {
        void fetchStart();
        void fetchComplete(JSONObject response);
        void fetchCancel();
    }
}
