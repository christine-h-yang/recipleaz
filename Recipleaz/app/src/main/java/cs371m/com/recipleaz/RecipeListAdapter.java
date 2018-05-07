package cs371m.com.recipleaz;

import android.content.Context;
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

    public class ResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView imageView;
        private String text;
        private String imageURL;

        public ResultsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = view.findViewById(R.id.recipe_pic);
            textView = view.findViewById(R.id.recipe_name);
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

        @Override
        public void onClick(View view) {
            Log.d("ONCLICK", "onClick: CLICKED SOMETHING");
            ClarifaiService.getInstance().processImage(imageURL);

            Recipe recipe = mData.get(getAdapterPosition());

            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra("recipe", recipe);
            context.startActivity(intent);
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
