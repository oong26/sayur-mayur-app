package com.example.sayurmayur.core;

import com.example.sayurmayur.model.CekIsFavorit;
import com.example.sayurmayur.model.GetIngredients;
import com.example.sayurmayur.model.GetRecipe;
import com.example.sayurmayur.model.Login;
import com.example.sayurmayur.model.Register;
import com.example.sayurmayur.model.TambahHapusFavorit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("newer-recipe")
    Call<GetRecipe> getNewRecipe();

    @GET("recipe")
    Call<GetRecipe> getRecipeHome();

    @GET("all-recipe")
    Call<GetRecipe> getAllRecipe();

    @GET("ingredients/{recipe_code}")
    Call<GetIngredients> getIngredients(@Path("recipe_code") String recipe_code);

    @GET("list-favorit/{id_user}")
    Call<GetRecipe> getFavorit(@Path("id_user") int id_user);

    @GET("check-favorit/{id_user}/{recipe_code}")
    Call<CekIsFavorit> checkIsFavorit(@Path("id_user") String id_user, @Path("recipe_code") String recipe_code);

    @FormUrlEncoded
    @POST("add-favorit")
    Call<TambahHapusFavorit> tambahFavorit(@Field("recipe_code") String recipe_code, @Field("id_user") int id_user);

    @GET("delete-favorit/{id_user}/{recipe_code}")
    Call<TambahHapusFavorit> hapusFavorit(@Path("id_user") int id_user, @Path("recipe_code") String recipe_code);

    @FormUrlEncoded
    @POST("login")
    Call<Login> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<Register> register(@Field("nama_lengkap") String nama, @Field("username") String username,  @Field("no_hp") String phone, @Field("password") String password);
}
