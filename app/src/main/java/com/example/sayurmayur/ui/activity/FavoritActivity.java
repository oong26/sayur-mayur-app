package com.example.sayurmayur.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sayurmayur.R;
import com.example.sayurmayur.core.ApiClient;
import com.example.sayurmayur.core.ApiInterface;
import com.example.sayurmayur.model.GetRecipe;
import com.example.sayurmayur.model.Recipe;
import com.example.sayurmayur.ui.adapter.RecipeHomeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritActivity extends AppCompatActivity {

    private ApiInterface mApiInterface;
    private SharedPreferences preferences;
    RecyclerView rvRecipe;
    ProgressBar progressBar;
    RecipeHomeAdapter resepHomeAdapter;
    EditText editSearch;
    TextView tvBelumAdaFavorit, tvResep;
    List<Recipe> listRecipe;
    List<Recipe> searchRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        initView();

        progressBar.setVisibility(View.VISIBLE);

        getRecipe();
        searchRecipe = new ArrayList<>();

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(searchRecipe.size() > 0) {
                        Log.d("cari", "kosongkan list pencarian");
                        searchRecipe.clear();
                    }
                    // melakukan pencarian
                    Log.d("cari", s.toString());
                    for(Recipe item:listRecipe) {
                        String data = item.getNama().toLowerCase();
                        if(data.contains(s.toString().toLowerCase())) {
                            searchRecipe.add(item);
                        }
                    }
                    resepHomeAdapter = new RecipeHomeAdapter(searchRecipe, FavoritActivity.this);
                    rvRecipe.setAdapter(resepHomeAdapter);
                }
                else {
                    // jika kolom pencarian kosong
                    Log.d("cari", "kolom kosong");
                    getRecipe();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        FavoritActivity.this.finish();
        return true;
    }

    private void initView() {
        this.progressBar = findViewById(R.id.progress_resep);
        tvBelumAdaFavorit = findViewById(R.id.tv_belum_punya_favorit);
        tvResep = findViewById(R.id.tv2);
        this.rvRecipe = findViewById(R.id.rv_recipe);
        this.editSearch = findViewById(R.id.src);
        this.rvRecipe.setLayoutManager(new LinearLayoutManager(FavoritActivity.this));
    }

    private void getRecipe() {
        int id_user = preferences.getInt("id_user", 0);
        Call<GetRecipe> callRecipe = mApiInterface.getFavorit(id_user);
        callRecipe.enqueue(new Callback<GetRecipe>() {
            @Override
            public void onResponse(Call<GetRecipe> call, Response<GetRecipe> response) {
                try {
                    listRecipe = response.body().getmRecipe();
                    if(listRecipe.size() > 0) {
                        resepHomeAdapter = new RecipeHomeAdapter(listRecipe, FavoritActivity.this);
                        rvRecipe.setAdapter(resepHomeAdapter);
                        tvBelumAdaFavorit.setVisibility(View.GONE);
                        tvResep.setVisibility(View.VISIBLE);
                        editSearch.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvBelumAdaFavorit.setVisibility(View.VISIBLE);
                        tvResep.setVisibility(View.GONE);
                        editSearch.setVisibility(View.GONE);
                    }
                }
                catch (Exception e) {
                    Log.e("getRecipe", e.getMessage());
                    Toast.makeText(FavoritActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetRecipe> call, Throwable t) {
                Log.e("getRecipe", t.getMessage());
                Toast.makeText(FavoritActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}