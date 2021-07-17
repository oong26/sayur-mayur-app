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

public class RecipeHomeAdapter extends RecyclerView.Adapter<RecipeHomeAdapter.MyViewHolder> {

    Context context;
    List<Recipe> listRecipe;
    
    public RecipeHomeAdapter(List<Recipe> listRecipe, Context context) {
        this.context = context;
        this.listRecipe = listRecipe;
    }
    
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe_home,parent,false);
        RecipeHomeAdapter.MyViewHolder myViewHolder = new RecipeHomeAdapter.MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return listRecipe.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String imgUrl = ApiClient.DOMAIN + listRecipe.get(position).getCover();
        Glide.with(context).load(imgUrl).into(holder.imgCover);
        holder.tvTitle.setText(listRecipe.get(position).getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goDetail = new Intent(context, DetailActivity.class);
                goDetail.putExtra("recipe_code", listRecipe.get(position).getRecipe_code());
                goDetail.putExtra("nama", listRecipe.get(position).getNama());
                goDetail.putExtra("cover", listRecipe.get(position).getCover());

                context.startActivity(goDetail);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgCover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_judul_new_resep);
            imgCover = itemView.findViewById(R.id.img_resep);
        }
    }
}
