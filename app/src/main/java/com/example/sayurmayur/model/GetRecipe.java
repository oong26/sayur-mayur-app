package com.example.sayurmayur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRecipe {
    @SerializedName("status")
    private int status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private List<Recipe> mRecipe;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Recipe> getmRecipe() {
        return mRecipe;
    }

    public void setmRecipe(List<Recipe> mRecipe) {
        this.mRecipe = mRecipe;
    }
}
