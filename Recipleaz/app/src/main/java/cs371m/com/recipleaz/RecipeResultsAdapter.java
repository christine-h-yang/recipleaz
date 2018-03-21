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
        View container;

        public ResultsViewHolder(View theView) {
            super(theView);
            container = theView;
            image = theView.findViewById(R.id.recipe_pic);
            text = theView.findViewById(R.id.recipe_name);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: launch new activity
                }
            });
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
        holder.text.setText(mData.get(position).title);
        Glide.with(context)
                .load(mData.get(position).imageURL)
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
