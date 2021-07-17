package com.example.sayurmayur.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.sayurmayur.model.Recipe;
import com.example.sayurmayur.ui.adapter.RecipeHomeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ApiInterface mApiInterface;
    private SwipeRefreshLayout swipeRefresh;
    RecyclerView rvRecipe;
    ProgressBar progressBar;
    RecipeHomeAdapter resepHomeAdapter;
    EditText editSearch;
    List<Recipe> listRecipe;
    List<Recipe> searchRecipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        initView(view);
        
        progressBar.setVisibility(View.VISIBLE);

//        getRecipe();
//        searchRecipe = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(this);
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                getRecipe();
                searchRecipe = new ArrayList<>();
            }
        });

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
                    resepHomeAdapter = new RecipeHomeAdapter(searchRecipe, getActivity());
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
    public void onRefresh() {
        getRecipe();
        searchRecipe = new ArrayList<>();
    }

    private void initView(View view) {
        this.swipeRefresh = view.findViewById(R.id.swipe_container);
        this.progressBar = view.findViewById(R.id.progress_resep);
        this.rvRecipe = view.findViewById(R.id.rv_recipe);
        this.editSearch = view.findViewById(R.id.src);
        this.rvRecipe.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getRecipe() {
        this.swipeRefresh.setRefreshing(true);
        Call<GetRecipe> callRecipe = mApiInterface.getAllRecipe();
        callRecipe.enqueue(new Callback<GetRecipe>() {
            @Override
            public void onResponse(Call<GetRecipe> call, Response<GetRecipe> response) {
                try {
                    listRecipe = response.body().getmRecipe();
                    resepHomeAdapter = new RecipeHomeAdapter(listRecipe, getActivity());
                    rvRecipe.setAdapter(resepHomeAdapter);
                }
                catch (Exception e) {
                    Log.e("getRecipe", e.getMessage());
                    Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    resepHomeAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetRecipe> call, Throwable t) {
                Log.e("getRecipe", t.getMessage());
                Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
