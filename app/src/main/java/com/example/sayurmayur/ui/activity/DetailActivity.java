package com.example.sayurmayur.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.sayurmayur.R;
import com.example.sayurmayur.core.ApiClient;
import com.example.sayurmayur.core.ApiInterface;
import com.example.sayurmayur.model.CekIsFavorit;
import com.example.sayurmayur.model.GetIngredients;
import com.example.sayurmayur.model.TambahHapusFavorit;
import com.example.sayurmayur.ui.adapter.IngredientsAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ApiInterface mApiInterface;
    private SharedPreferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Intent mData;
    private ProgressBar progressBar;
    private ImageView imgCover;
    private TextView tvJudul;
    private RecyclerView rvBahan;
    private Button btnTambahFavorit, btnHapusFavorit;
    private IngredientsAdapter ingredientsAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressDialog = new ProgressDialog(DetailActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = DetailActivity.this.getSharedPreferences("login_session", Context.MODE_PRIVATE);
        mData = DetailActivity.this.getIntent();

        initView();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getIngredients();

                setData();

                if(preferences.getBoolean("is_login", false)) {
                    // ketika sudah login
                    checkIsFavorit();
                    btnHapusFavorit.setVisibility(View.GONE);
                }
            }
        });

//        getIngredients();
//
//        setData();

//        if(preferences.getBoolean("is_login", false)) {
//            // ketika sudah login
//            checkIsFavorit();
//            btnHapusFavorit.setVisibility(View.GONE);
//        }

        btnTambahFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mohon tunggu");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if(preferences.getBoolean("is_login", false)) {
                    // ketika sudah login
                    tambahFavorit();
                }
                else {
                    // ketika belum login
                    Toast.makeText(DetailActivity.this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        btnHapusFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusFavorit();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        DetailActivity.this.finish();
        return true;
    }

    @Override
    public void onRefresh() {
        getIngredients();

        setData();

        if(preferences.getBoolean("is_login", false)) {
            // ketika sudah login
            checkIsFavorit();
            btnHapusFavorit.setVisibility(View.GONE);
        }
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        imgCover = findViewById(R.id.img_cover);
        tvJudul = findViewById(R.id.judul);
        progressBar = findViewById(R.id.progress_detail);
        rvBahan = findViewById(R.id.rv_bahan);
        rvBahan.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        btnTambahFavorit = findViewById(R.id.btn_tambah);
        btnHapusFavorit = findViewById(R.id.btn_hapus);
        btnTambahFavorit.setVisibility(View.GONE);
        btnHapusFavorit.setVisibility(View.GONE);
    }

    private void setData() {
        String imgUrl = ApiClient.DOMAIN + mData.getStringExtra("cover");
        Glide.with(DetailActivity.this).load(imgUrl).into(imgCover);

        tvJudul.setText(mData.getStringExtra("nama"));

    }

    private void getIngredients() {
        Call<GetIngredients> callIngredients = mApiInterface.getIngredients(mData.getStringExtra("recipe_code"));
        callIngredients.enqueue(new Callback<GetIngredients>() {
            @Override
            public void onResponse(Call<GetIngredients> call, Response<GetIngredients> response) {
                try {
                    ingredientsAdapter = new IngredientsAdapter(response.body().getmIngredients(), DetailActivity.this);
                    rvBahan.setAdapter(ingredientsAdapter);
                }catch (Exception e) {
                    Log.e("getIngredients", e.getMessage());
                    Toast.makeText(DetailActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetIngredients> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("getIngredients", t.getMessage());
                Toast.makeText(DetailActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIsFavorit() {
        swipeRefreshLayout.setRefreshing(true);
        String id_user = String.valueOf(preferences.getInt("id_user", 0));
        String recipe_code = mData.getStringExtra("recipe_code");
        Call<CekIsFavorit> cekIsFavoritCall = mApiInterface.checkIsFavorit(id_user, recipe_code);
        cekIsFavoritCall.enqueue(new Callback<CekIsFavorit>() {
            @Override
            public void onResponse(Call<CekIsFavorit> call, Response<CekIsFavorit> response) {
                try {
                    if(response.body().getStatus() == 200) {
                        if(response.body().getMessage().equals("Sudah favorit")) {
                            btnTambahFavorit.setVisibility(View.GONE);
                            btnHapusFavorit.setVisibility(View.VISIBLE);
                        }
                        if(response.body().getMessage().equals("Belum favorit")) {
                            btnTambahFavorit.setVisibility(View.VISIBLE);
                            btnHapusFavorit.setVisibility(View.GONE);
                        }
                    }
                }
                catch (Exception e) {
                    Log.e("checkIsFavorit", e.getMessage());
                    Toast.makeText(DetailActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<CekIsFavorit> call, Throwable t) {
                Log.e("checkIsFavorit", t.getMessage());
                Toast.makeText(DetailActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void tambahFavorit() {
        Call<TambahHapusFavorit> tambahHapusFavoritCall = mApiInterface.tambahFavorit(mData.getStringExtra("recipe_code"), preferences.getInt("id_user", 0));
        tambahHapusFavoritCall.enqueue(new Callback<TambahHapusFavorit>() {
            @Override
            public void onResponse(Call<TambahHapusFavorit> call, Response<TambahHapusFavorit> response) {
                try {
                    if(response.body().getStatus() == 200) {
                        Toast.makeText(DetailActivity.this, "Berhasil menambah ke favorit", Toast.LENGTH_SHORT).show();
                        checkIsFavorit();
                    }
                    else {
                        Toast.makeText(DetailActivity.this, "Gagal menambah ke favorit", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("tambahFavorit", e.getMessage());
                    Toast.makeText(DetailActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TambahHapusFavorit> call, Throwable t) {
                Log.e("tambahFavorit", t.getMessage());
                Toast.makeText(DetailActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void hapusFavorit() {
        progressDialog.setMessage("Mohon tunggu");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<TambahHapusFavorit> hapusFavoritCall = mApiInterface.hapusFavorit(preferences.getInt("id_user", 0), mData.getStringExtra("recipe_code" ));
        hapusFavoritCall.enqueue(new Callback<TambahHapusFavorit>() {
            @Override
            public void onResponse(Call<TambahHapusFavorit> call, Response<TambahHapusFavorit> response) {
                try {
                    if(response.body().getStatus() == 200) {
                        if (response.body().getMessage().equals("Berhasil")) {
                            Toast.makeText(DetailActivity.this, "Berhasil menghapus dari favorit", Toast.LENGTH_SHORT).show();
                            checkIsFavorit();
                        }
                        else {
                            Toast.makeText(DetailActivity.this, "Gagal menghapus favorit", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e) {
                    Log.e("hapusFavorit", e.getMessage());
                    Toast.makeText(DetailActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TambahHapusFavorit> call, Throwable t) {
                Log.e("hapusFavorit", t.getMessage());
                Toast.makeText(DetailActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}