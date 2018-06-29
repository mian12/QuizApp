package com.solution.alnahar.quizapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.solution.alnahar.quizapp.Interface.ItemClickListener;
import com.solution.alnahar.quizapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView category_Image;
    public TextView category_TextView;

    ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        category_Image = itemView.findViewById(R.id.category_Image);
        category_TextView = itemView.findViewById(R.id.categry_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);


    }
}
