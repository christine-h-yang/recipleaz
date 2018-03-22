package cs371m.com.recipleaz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RecipeResultsAdapter extends RecyclerView.Adapter<RecipeResultsAdapter.ResultsViewHolder> {
    private ArrayList<Recipe> mData = new ArrayList<>();
    private Context context;

    public class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image;

        public ResultsViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.recipe_pic);
            text = view.findViewById(R.id.recipe_name);
        }
    }

    public RecipeResultsAdapter(Context _context) {
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
        Log.d("TESTING", "onBindViewHolder: binding " + recipe);

        holder.text.setText(recipe.title);
        Glide.with(context)
                .load(recipe.imageURL)
                .into(holder.image);
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
}
