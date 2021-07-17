package com.example.sayurmayur.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sayurmayur.R;
import com.example.sayurmayur.core.ApiClient;
import com.example.sayurmayur.core.ApiInterface;
import com.example.sayurmayur.model.GetRecipe;
import com.example.sayurmayur.ui.adapter.NewerRecipeAdapter;
import com.example.sayurmayur.ui.adapter.RecipeHomeAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ApiInterface mApiInterface;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressNewResep, progressResep;
    RecyclerView rvNewerRecipe, rvKategori;
    NewerRecipeAdapter newerRecipeAdapter;
    RecipeHomeAdapter resepHomeAdapter;
    private TextView tvResepBaru, tvSelengkapnya;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        initView(view);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                progressNewResep.setVisibility(View.VISIBLE);
                progressResep.setVisibility(View.VISIBLE);
                getNewRecipe();
                getRecipe();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tvSelengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RecipeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//                getContext().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        progressNewResep.setVisibility(View.VISIBLE);
        progressResep.setVisibility(View.VISIBLE);
        getNewRecipe();
        getRecipe();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initView(View view) {
        this.swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        this.progressNewResep = view.findViewById(R.id.progress_new_resep);
        this.progressResep = view.findViewById(R.id.progress_resep);

        this.tvResepBaru = view.findViewById(R.id.tv1);
        this.tvSelengkapnya = view.findViewById(R.id.tv3);
        this.rvNewerRecipe = view.findViewById(R.id.rv_new_resep);
        this.rvKategori = view.findViewById(R.id.rv_kategori);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        this.rvNewerRecipe.setLayoutManager(layoutManager);

        this.rvKategori.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getNewRecipe() {
        Call<GetRecipe> callBuku = mApiInterface.getNewRecipe();
        callBuku.enqueue(new Callback<GetRecipe>() {
            @Override
            public void onResponse(Call<GetRecipe> call, Response<GetRecipe> response) {
                try {
                    if(response.body().getmRecipe().size() > 0) {
                        // data ada
                        tvResepBaru.setVisibility(View.VISIBLE);
                        rvNewerRecipe.setVisibility(View.VISIBLE);
                        newerRecipeAdapter = new NewerRecipeAdapter(response.body().getmRecipe(), getActivity());
                        rvNewerRecipe.setAdapter(newerRecipeAdapter);
                    }
                    else {
                        // data belum ada
                        tvResepBaru.setVisibility(View.GONE);
                        rvNewerRecipe.setVisibility(View.GONE);
                    }
                }
                catch (Exception e) {
                    Log.e("getTerfavorit", e.getMessage());
                    Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressNewResep.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetRecipe> call, Throwable t) {
                Log.e("getTerfavorit", t.getMessage());
                Toast.makeText(getActivity(), "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                progressNewResep.setVisibility(View.GONE);
            }
        });
    }

    private void getRecipe() {
        Call<GetRecipe> callKategori = mApiInterface.getRecipeHome();
        callKategori.enqueue(new Callback<GetRecipe>() {
            @Override
            public void onResponse(Call<GetRecipe> call, Response<GetRecipe> response) {
                try {
                    resepHomeAdapter = new RecipeHomeAdapter(response.body().getmRecipe(), getActivity());
                    rvKategori.setAdapter(resepHomeAdapter);
                }
                catch (Exception e) {
                    Log.e("getKategori", e.getMessage());
                    Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressResep.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetRecipe> call, Throwable t) {
                Log.e("getKategori", t.getMessage());
                Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                progressResep.setVisibility(View.GONE);
            }
        });
    }
}
