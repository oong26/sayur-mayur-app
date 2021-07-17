package com.example.sayurmayur.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sayurmayur.R;
import com.example.sayurmayur.core.ApiClient;
import com.example.sayurmayur.model.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    Context context;
    List<Ingredients> listIngredients;

    public IngredientsAdapter(List<Ingredients> listIngredient, Context context) {
        this.context = context;
        this.listIngredients = listIngredient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient,parent,false);
        IngredientsAdapter.MyViewHolder myViewHolder = new IngredientsAdapter.MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return listIngredients.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String imgUrl = ApiClient.DOMAIN + listIngredients.get(position).getCover();
        Glide.with(context).load(imgUrl).into(holder.imgCover);
        holder.tvTitle.setText(listIngredients.get(position).getName());
        holder.tvDose.setText(listIngredients.get(position).getDose());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDose;
        ImageView imgCover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_judul);
            tvDose = itemView.findViewById(R.id.tv_takaran);
            imgCover = itemView.findViewById(R.id.img_bahan);
        }
    }
}
