package com.example.sayurmayur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetIngredients {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Ingredients> mIngredients;

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

    public List<Ingredients> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(List<Ingredients> mIngredients) {
        this.mIngredients = mIngredients;
    }
}
