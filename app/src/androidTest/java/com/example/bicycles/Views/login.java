package com.example.bicycles.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bicycles.Model.LoginRequest;
import com.example.bicycles.R;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    private EditText etCorreo, etContrasena;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);


        btnIniciarSesion.setOnClickListener(view -> {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                iniciarSesion(correo, contrasena);
            }
        });


        TextView crearCuentaTextView = findViewById(R.id.tv_crear_cuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        crearCuentaTextView.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });
    }


    private void iniciarSesion(String correo, String contrasena) {
        LoginRequest request = new LoginRequest(correo, contrasena);

        RetrofitClient.getInstance()
                .getApiService()
                .login(request)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(login.this, "Bienvenido " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(login.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
