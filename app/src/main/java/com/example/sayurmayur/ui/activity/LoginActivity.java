package com.example.sayurmayur.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sayurmayur.R;
import com.example.sayurmayur.core.ApiClient;
import com.example.sayurmayur.core.ApiInterface;
import com.example.sayurmayur.model.Login;
import com.example.sayurmayur.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ApiInterface mApiInterface;
    private TextView tvDaftar;
    private EditText editUsername, editPassword;
    private Button btnLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        initView();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mohon tunggu");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if(editUsername.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Harap isi username terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else if(editPassword.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Harap isi password terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else {
                    prosesLogin(editUsername.getText().toString(), editPassword.getText().toString());
                }
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void initView() {
        tvDaftar = findViewById(R.id.tv_belum_punya_akun);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void prosesLogin(String username, String password) {
        Call<Login> callLogin = mApiInterface.login(username, password);
        callLogin.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    if(response.body().getStatus() == 200) {
                        if(response.body().getMessage().equals("Berhasil")) {
                            saveLoginSession(response.body().getmUser());

                            LoginActivity.this.finish();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else if(response.body().getMessage().contains("Password salah"))
                            Toast.makeText(LoginActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this, "Akun tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("prosesLogin", e.getMessage());
                    Toast.makeText(LoginActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
                finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("prosesLogin", t.getMessage());
                Toast.makeText(LoginActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginSession(User data) {
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("login_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id_user", data.getId());
        editor.putString("nama", data.getNama());
        editor.putString("username", data.getUsername());
        editor.putString("phone", data.getPhone());
        editor.putBoolean("is_login", true);
        editor.apply();
    }
}