package com.example.bicycles.Views;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.CountDownTimer;

import com.example.bicycles.Models.Token;
import com.example.bicycles.R;


public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                SharedPreferences token = getSharedPreferences("token", MODE_PRIVATE);
                String tokenStr = token.getString("token", null);
                if(tokenStr != null){
                    Intent intent = new Intent( splash.this, Home.class );
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent( splash.this, login.class );
                    startActivity(intent);
                    finish();
                }
            }
        }.start();

    }
}