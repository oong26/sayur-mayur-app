package com.example.sayurmayur.ui.activity;

import android.content.Intent;
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
import com.example.sayurmayur.model.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    private EditText editNama, editUsername, editPhone, editPassword;
    private TextView tvLogin;
    private Button btnRegister;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initView();
        
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNama.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap isi nama terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else if(editUsername.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap isi username terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else if(editPhone.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap isi nomor telepon terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else if(editPassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap isi password terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else {
                    prosesRegister(editNama.getText().toString(), editUsername.getText().toString(), editPhone.getText().toString(), editPassword.getText().toString());
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void initView() {
        editNama = findViewById(R.id.edit_nama);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_phone);
        editPassword = findViewById(R.id.edit_password);
        tvLogin = findViewById(R.id.tv_sudah_punya_akun);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void prosesRegister(String nama, String username, String phone, String password) {
        Call<Register> callRegister = mApiInterface.register(nama, username, phone, password);
        callRegister.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                try {
                    if(response.body().getStatus() == 200) {
                        if(response.body().getMessage().equals("Berhasil")) {
                            RegisterActivity.this.finish();

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            Toast.makeText(RegisterActivity.this, "Berhasil mendaftar", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Gagal mendaftarkan akun", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Server tidak terjangkau", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("prosesLogin", e.getMessage());
                    Toast.makeText(RegisterActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.e("prosesLogin", t.getMessage());
                Toast.makeText(RegisterActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}