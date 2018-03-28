package cs371m.com.recipleaz;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    public String imageURL;
    public String[] ingredients;
    public String instructionsURL;
    public String title;

    public Recipe(String imageURL, String[] ingredients, String instructionsURL, String title) {
        this.imageURL = imageURL;
        this.ingredients = ingredients;
        this.instructionsURL = instructionsURL;
        this.title = title;
    }

    public Recipe(Parcel in) {
        imageURL = in.readString();
        ingredients = in.createStringArray();
        instructionsURL = in.readString();
        title = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageURL);
        dest.writeStringArray(ingredients);
        dest.writeString(instructionsURL);
        dest.writeString(title);
    }

    public String toString() {
        return title + ": " + imageURL;
    }
}
