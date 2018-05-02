package cs371m.com.recipleaz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    public String imageURL;
    public int yield;
    public List<String> ingredients;
    public String instructionsURL;
    public String title;

    public Recipe(String imageURL, int yield, List<String> ingredients, String instructionsURL, String title) {
        this.imageURL = imageURL;
        this.yield = yield;
        this.ingredients = ingredients;
        this.instructionsURL = instructionsURL;
        this.title = title;
    }

    public Recipe(Parcel in) {
        imageURL = in.readString();
        yield = in.readInt();
        ingredients = new ArrayList<>();
        in.readList(ingredients, String.class.getClassLoader());
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
        dest.writeInt(yield);
        dest.writeList(ingredients);
        dest.writeString(instructionsURL);
        dest.writeString(title);
    }

    public String toString() {
        return title + ": " + imageURL;
    }
}
