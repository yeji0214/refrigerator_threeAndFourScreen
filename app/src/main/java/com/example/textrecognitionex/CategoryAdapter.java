package com.example.textrecognitionex;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private CategoryViewModel viewModel;

    public CategoryAdapter(CategoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // ViewHolder Class - 아이템 뷰를 저장
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.category = itemView.findViewById(R.id.category);
            this.category.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e("Category ViewHolder", "Long Click Position = " + getAdapterPosition());
                    viewModel.longClickPosition = getAdapterPosition();
                    return false;
                }
            });

        }

        public void setContents(int pos) {
            String text = viewModel.categorys.get(pos);
            category.setText(text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.category_recyclerview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = viewModel.categorys.get(position);
        holder.setContents(position);
    }

    @Override
    public int getItemCount() {
        return viewModel.getCategorySize();
    }
}
