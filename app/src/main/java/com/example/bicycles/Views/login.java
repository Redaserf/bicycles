package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.R;
import com.example.bicycles.ViewModels.LoginViewModel;

public class login extends AppCompatActivity {
    private EditText etCorreo, etContrasena;
    private Button btnIniciarSesion;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);

        etContrasena.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Detectar si se tocó el ícono del "ojito"
                if (event.getRawX() >= (etContrasena.getRight() - etContrasena.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;

                    if (isPasswordVisible) {
                        etContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etContrasena.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24,
                                0,
                                R.drawable.baseline_visibility_24, // Ícono ojito abierto
                                0
                        );
                    } else {
                        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etContrasena.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24,
                                0,
                                R.drawable.baseline_visibility_off_24, // Ícono ojito cerrado
                                0
                        );
                    }
                    // Mover cursor al final del texto
                    etContrasena.setSelection(etContrasena.getText().length());
                    return true;
                }
            }
            return false;
        });

        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        btnIniciarSesion.setOnClickListener(view -> {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                loginViewModel.login(correo, contrasena, login.this);
//                iniciarSesion(correo, contrasena);
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


//    private void iniciarSesion(String correo, String contrasena) {
//        LoginRequest request = new LoginRequest(correo, contrasena);
//
//        RetrofitClient.getInstance()
//                .getApiService()
//                .login(request)
//                .enqueue(new Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            Toast.makeText(login.this, "Bienvenido " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResponse> call, Throwable t) {
//                        Toast.makeText(login.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
