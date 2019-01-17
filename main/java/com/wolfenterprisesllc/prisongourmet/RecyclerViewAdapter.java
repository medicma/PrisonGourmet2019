package com.wolfenterprisesllc.prisongourmet;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter
        <RecyclerViewAdapter.ListItemViewHolder> {

    private List<RecipieHolder> items;

    RecyclerViewAdapter(List<RecipieHolder> items) {
        this.items = items;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements RecyclerTouchListener.ClickListener {
        TextView recipie;

        ListItemViewHolder(View view) {
            super(view);
            recipie = view.findViewById(R.id.txt_recipie);
            recipie.setTypeface(null, Typeface.ITALIC);
        }

        @Override
        public void onClick(View view, int position) {
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.common_item_layout, parent, false);
        return new RecyclerViewAdapter.ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ListItemViewHolder holder, int position) {
        try {
            holder.recipie.setText(items.get(position).getRecipie());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (Exception e) {
            return items.size();
        }
    }
}


