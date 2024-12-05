package com.example.bicycles.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Token;
import com.example.bicycles.R;
import com.example.bicycles.Responses.SensoresResponse;
import com.example.bicycles.ViewModels.SensoresViewModel;
import com.example.bicycles.Views.Fragments.MasFragment;

import java.util.List;


public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("Splash", "Intentando redirigir...");

//        new CountDownTimer(3000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                Intent intent = new Intent(splash.this, login.class);
//                startActivity(intent);
//                finish();
//            }
//        }.start();

        new CountDownTimer(2000, 1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                try {
                    SharedPreferences tokenPref = getSharedPreferences("token", MODE_PRIVATE);
                    String tokenStr = tokenPref.getString("token", null);

                    Log.d("Splash", "Token encontrado: " + tokenStr);

                    if (tokenStr != null) {
                        Intent intent = new Intent(splash.this, Home.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(splash.this, login.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("Splash", "Error en Splash: " + e.getMessage(), e);
                    Toast.makeText(context,"Ha ocurrido un error. Redirigiendo a Login...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(splash.this, login.class);
                    startActivity(intent);
                } finally {
                    finish();
                }

            }
        }.start();

    }
}