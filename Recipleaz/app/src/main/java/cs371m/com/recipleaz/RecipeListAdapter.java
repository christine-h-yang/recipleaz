package cs371m.com.recipleaz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ResultsViewHolder> {
    private ArrayList<Recipe> mData = new ArrayList<>();
    private Context context;
    private boolean isSavedRecipesList = false;
    private DeleteRecipeInterface deleteRecipeInterface;

    interface DeleteRecipeInterface {
        void deleteSavedRecipe(Recipe recipe);
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private TextView textView;
        private ImageView imageView;
        private TextView yieldView;
        private String text;
        private String yield;
        private String imageURL;

        public ResultsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            imageView = view.findViewById(R.id.recipe_pic);
            textView = view.findViewById(R.id.recipe_name);
            yieldView = view.findViewById(R.id.listview_item_short_description);
        }

        public void setText(String text) {
            this.text = text;
            textView.setText(text);
        }

        public void setImage(String imageURL) {
            this.imageURL = imageURL;
            Glide.with(context)
                    .load(imageURL)
                    .into(imageView);
        }

        public void setYield(String text) {
            this.yield = text;
            yieldView.setText(text);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = mData.get(getAdapterPosition());

            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra("recipe", recipe);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            if (!isSavedRecipesList) {
                return true;
            }
            AlertDialog confirmDeleteDialog = new AlertDialog.Builder(context)
                    .setTitle("Delete this recipe?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteRecipeInterface.deleteSavedRecipe(mData.get(getAdapterPosition()));
                            removeItem(getAdapterPosition());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            confirmDeleteDialog.show();
            return true;
        }
    }

    public RecipeListAdapter(Context _context) {
        this.context = _context;
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_result_row, parent, false);

        ResultsViewHolder vh = new ResultsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, final int position) {
        Recipe recipe = mData.get(position);

        holder.setText(recipe.title);
        holder.setImage(recipe.imageURL);
        holder.setYield("This recipe yields " + recipe.yield + " servings");
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(List<Recipe> recipes) {
        mData.addAll(recipes);
        notifyDataSetChanged();
    }

    public void setIsSavedRecipesList(boolean isSavedRecipesList) {
        this.isSavedRecipesList = isSavedRecipesList;
    }

    public void setDeleteRecipeInterface(DeleteRecipeInterface deleteRecipeInterface) {
        this.deleteRecipeInterface = deleteRecipeInterface;
    }

    private void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}
