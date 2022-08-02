package com.example.textrecognitionex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ItemViewModel viewModel;

    public ItemAdapter(ItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextView;
        TextView itemTextView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemTextView = itemView.findViewById(R.id.itemTextView);
            this.itemTextView2 = itemView.findViewById(R.id.itemTextView2);

            this.itemTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewModel.longClickPosition = getAdapterPosition();
                    return false;
                }
            });
            this.itemTextView2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewModel.longClickPosition = getAdapterPosition();
                    return false;
                }
            });
        }

        public void setContents(int pos) {
            String text = viewModel.items.get(pos);
            itemTextView.setText(text);
            itemTextView2.setText(text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_recyclerview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = viewModel.items.get(position);
        holder.setContents(position);
    }

    @Override
    public int getItemCount() {
        return viewModel.getItemSize();
    }
}
