package com.example.sayurmayur.model;

import com.google.gson.annotations.SerializedName;

public class Ingredients {
    @SerializedName("product_code")
    private String product_code;
    @SerializedName("name")
    private String name;
    @SerializedName("dose")
    private String dose;
    @SerializedName("cover")
    private String cover;

    public Ingredients(String product_code, String name, String dose, String cover) {
        this.product_code = product_code;
        this.name = name;
        this.dose = dose;
        this.cover = cover;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
