package com.example.sayurmayur.model;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("recipe_code")
    private String recipe_code;
    @SerializedName("name")
    private String nama;
    @SerializedName("cover")
    private String cover;

    public Recipe(String recipe_code, String nama, String cover) {
        this.recipe_code = recipe_code;
        this.nama = nama;
        this.cover = cover;
    }

    public String getRecipe_code() {
        return recipe_code;
    }

    public void setRecipe_code(String recipe_code) {
        this.recipe_code = recipe_code;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
