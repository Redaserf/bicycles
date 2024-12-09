package com.example.bicycles.Views;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bicycles.R;

public class splash extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Manejar el diseño y la pantalla completa
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("Splash", "Comenzando flujo de Splash...");

        // Crear el canal de notificaciones
        createNotificationChannel();

        // Solicitar permiso de notificaciones si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                proceedToNextScreen();
            }
        } else {
            proceedToNextScreen();
        }
    }

    // Método para avanzar a la siguiente pantalla después del permiso
    private void proceedToNextScreen() {
        Log.d("Splash", "Intentando redirigir a la siguiente pantalla...");

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // No hacer nada aquí
            }

            @Override
            public void onFinish() {
                try {
                    SharedPreferences tokenPref = getSharedPreferences("token", MODE_PRIVATE);
                    String tokenStr = tokenPref.getString("token", null);

                    Intent intent;
                    if (tokenStr != null) {
                        // Redirigir al Home con el fragmento "mas_fragment"
                        intent = new Intent(splash.this, Home.class);
                        intent.putExtra("load_fragment", "mas_fragment");
                    } else {
                        // Redirigir al login
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

    // Manejar la respuesta del permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
            }
            proceedToNextScreen(); // Avanzar a la siguiente pantalla independientemente del resultado
        }
    }

    // Crear el canal de notificaciones
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Notificaciones de Inactividad";
            String channelDescription = "Recordatorios para abrir la app después de inactividad.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("reminder_channel", channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}