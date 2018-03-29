package cs371m.com.recipleaz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private ArrayList<Ingredient> mData = new ArrayList<>();
    private Context context;

    public class IngredientsViewHolder extends RecyclerView.ViewHolder  {
        private CheckedTextView ingredient;

        public IngredientsViewHolder(View view) {
            super(view);
            ingredient = view.findViewById(R.id.ingredient);

            ingredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ingredient.toggle();
                    boolean isChecked = ingredient.isChecked();
                    mData.get(getAdapterPosition()).isChecked = isChecked;
                }
            });
        }
    }

    public IngredientsAdapter(Context _context) {
        this.context = _context;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ingredient, parent, false);

        IngredientsViewHolder vh = new IngredientsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, final int position) {
        Ingredient ingredient = mData.get(position);

        holder.ingredient.setText(ingredient.name);
        holder.ingredient.setChecked(ingredient.isChecked);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(List<Ingredient> ingredients) {
        mData.addAll(ingredients);
        notifyDataSetChanged();
    }
}
