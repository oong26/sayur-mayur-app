package com.example.sayurmayur.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sayurmayur.R;
import com.example.sayurmayur.ui.activity.FavoritActivity;
import com.example.sayurmayur.ui.activity.LoginActivity;
import com.example.sayurmayur.ui.activity.MainActivity;

public class PengaturanFragment extends Fragment {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView tvNama, tvUsername, tvPhone;
    private Button btnFav, btnLogout, btnLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pengaturan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkLogin();

        initView(view);

        if(preferences.getBoolean("is_login", false)) {
            // ketika sudah login
            tvNama.setVisibility(View.VISIBLE);
            tvNama.setText(preferences.getString("nama", null));
            tvUsername.setVisibility(View.VISIBLE);
            tvUsername.setText(preferences.getString("username", null));
            tvPhone.setVisibility(View.VISIBLE);
            tvPhone.setText(preferences.getString("phone", null));

            btnLogin.setVisibility(View.GONE);
            btnFav.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        }
        else {
            // ketika belum login
            tvNama.setVisibility(View.GONE);
            tvUsername.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnFav.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoritActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(getActivity(), LoginActivity.class);
                goLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goLogin);
                getActivity().finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("id_user");
                editor.remove("nama");
                editor.remove("username");
                editor.remove("phone");
                editor.putBoolean("is_login", false);
                editor.apply();

                Intent goLogin = new Intent(getActivity(), MainActivity.class);
                goLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                startActivity(goLogin);
            }
        });
    }

    private void initView(View view) {
        btnFav = view.findViewById(R.id.btn_favorit);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogin = view.findViewById(R.id.btn_login);
        tvNama = view.findViewById(R.id.tv_nama);
        tvUsername = view.findViewById(R.id.tv_username);
        tvPhone = view.findViewById(R.id.tv_phone);
    }

    private void checkLogin() {
        preferences = getActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
}
