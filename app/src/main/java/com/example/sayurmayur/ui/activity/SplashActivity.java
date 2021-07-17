package com.example.sayurmayur.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sayurmayur.R;
import com.example.sayurmayur.core.AppController;

public class SplashActivity extends AppCompatActivity {

    private boolean connectInternet;
    private boolean isNetworkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new InitializingDataTask().execute();
    }

    private class InitializingDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            //Saat proses baru mulai
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Proses background
            connectInternet = AppController.isNetworkConnected(SplashActivity.this);
            isNetworkAvailable = AppController.isInternetAvailable();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Jika proses selesai
            super.onPostExecute(aVoid);
            if(connectInternet && isNetworkAvailable){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            else if(!connectInternet){
                AppController.showMessage(SplashActivity.this, "Anda sedang offline. Mohon hubungkan perangkat anda ke jaringan.", 1);
            }
            else if(!isNetworkAvailable){
                AppController.showMessage(SplashActivity.this, "Server tidak terjangkau. Mohon periksa koneksi internet anda", 1);
            }

            SplashActivity.this.finish();
        }
    }
}