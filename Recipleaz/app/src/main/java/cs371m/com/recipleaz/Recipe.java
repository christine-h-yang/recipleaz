package cs371m.com.recipleaz;

public class Recipe {
    public String imageURL;
    public String[] ingredients;
    public String instructionsURL;
    public String title;

    public String toString() {
        return title + ": " + imageURL;
    }
}
