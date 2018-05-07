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
    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<Boolean> ingredientsChecklist = new ArrayList<>();
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
                    ingredientsChecklist.set(getAdapterPosition(), isChecked);
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
        holder.ingredient.setText(ingredients.get(position));
        holder.ingredient.setChecked(ingredientsChecklist.get(position));
    }

    @Override
    public long getItemId(int position) {
        return ingredients.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void add(List<String> ingredients, List<Boolean> ingredientsChecklist) {
        this.ingredients.addAll(ingredients);
        this.ingredientsChecklist.addAll(ingredientsChecklist);
        notifyDataSetChanged();
    }

    public List<Boolean> getChecklist() {
        return ingredientsChecklist;
    }
}
