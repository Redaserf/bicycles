package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.LoginViewModel;

public class login extends AppCompatActivity {
    private EditText etCorreo, etContrasena;
    private Button btnIniciarSesion;
    private boolean isPasswordVisible = false; // Controlar visibilidad de la contraseña

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvErrorMessage = findViewById(R.id.tv_error_messages); // Usar el nuevo ID
        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);

        // Configurar funcionalidad del ícono del ojo
        etContrasena.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Detectar si se tocó el ícono del ojo (en el lado derecho)
                if (event.getRawX() >= (etContrasena.getRight() - etContrasena.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;

                    if (isPasswordVisible) {
                        etContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etContrasena.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24, // Ícono de candado
                                0,
                                R.drawable.baseline_visibility_24, // Ícono de ojo abierto
                                0
                        );
                    } else {
                        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etContrasena.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24, // Ícono de candado
                                0,
                                R.drawable.baseline_visibility_off_24, // Ícono de ojo cerrado
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

        Factory factory = new Factory(this);
        LoginViewModel loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        btnIniciarSesion.setOnClickListener(view -> {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            // Validación de campos vacíos
            if (correo.isEmpty() || contrasena.isEmpty()) {
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText("Por favor, rellena todos los campos."); // Mensaje de error para campos vacíos
                hideErrorMessageAfterDelay(tvErrorMessage, 5000); // Ocultar después de 10 segundos
                return;
            }

            // Intentar iniciar sesión
            loginViewModel.login(correo, contrasena);
            loginViewModel.getLoginMessage().observe(this, message -> {
                if ("REDIRECT".equals(message)) {
                    tvErrorMessage.setVisibility(View.GONE); // Ocultar mensaje de error si el login es exitoso
                    Intent intent = new Intent(this, Home.class);
                    intent.putExtra("load_fragment", "mas_fragment"); // Indicar que debe cargar MasFragment
                    startActivity(intent);
                    finish();
                } else if (message != null) {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText("Credenciales inválidas. Inténtalo de nuevo."); // Mensaje de error para credenciales inválidas
                    hideErrorMessageAfterDelay(tvErrorMessage, 5000); // Ocultar después de 10 segundos
                }
            });
        });

        TextView crearCuentaTextView = findViewById(R.id.tv_crear_cuenta);
        crearCuentaTextView.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });
    }

    // Método para ocultar el mensaje de error después de un retraso
    private void hideErrorMessageAfterDelay(TextView tvErrorMessage, int delayMillis) {
        new Handler().postDelayed(() -> tvErrorMessage.setVisibility(View.GONE), delayMillis);
    }
}
