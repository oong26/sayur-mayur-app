package com.example.sayurmayur.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.sayurmayur.model.Recipe;
import com.example.sayurmayur.ui.activity.DetailActivity;

import java.util.List;

public class NewerRecipeAdapter extends RecyclerView.Adapter<NewerRecipeAdapter.TerfavoritViewHolder> {

    Context context;
    List<Recipe> mList;

    public NewerRecipeAdapter(List<Recipe> recipe, Context context) {
        this.mList = recipe;
        this.context = context;
    }

    @NonNull
    @Override
    public TerfavoritViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favorit,parent,false);
        TerfavoritViewHolder myViewHolder = new TerfavoritViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TerfavoritViewHolder holder, final int position) {
        String imgUrl = ApiClient.DOMAIN + mList.get(position).getCover();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.imgCover);
        holder.tvJudul.setText(mList.get(position).getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goDetail = new Intent(context, DetailActivity.class);
                goDetail.putExtra("recipe_code", mList.get(position).getRecipe_code());
                goDetail.putExtra("nama", mList.get(position).getNama());
                goDetail.putExtra("cover", mList.get(position).getCover());

                context.startActivity(goDetail);
            }
        });
    }

    class TerfavoritViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul;
        ImageView imgCover;

        public TerfavoritViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_favorit);
            imgCover = itemView.findViewById(R.id.img_cover);
        }
    }
}