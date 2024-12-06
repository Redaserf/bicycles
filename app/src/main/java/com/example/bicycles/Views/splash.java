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



        new CountDownTimer(2000, 1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                try {
                    SharedPreferences tokenPref = getSharedPreferences("token", MODE_PRIVATE);
                    String tokenStr = tokenPref.getString("token", null);

                    Intent intent;

                    if (tokenStr != null) {
                        // Si el token existe, redirigimos al Home con el fragmento "mas_fragment"
                        intent = new Intent(splash.this, Home.class);
                        intent.putExtra("load_fragment", "mas_fragment");
                    } else {
                        // Si no hay token, redirigimos al login
                        intent = new Intent(splash.this, login.class);
                    }

                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Splash", "Error en Splash: " + e.getMessage(), e);
                    Intent intent = new Intent(splash.this, login.class);
                    startActivity(intent);
                } finally {
                    finish();
                }
            }


        }.start();

    }
}