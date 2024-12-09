package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.LoginViewModel;
import com.example.bicycles.code_verify;

public class login extends AppCompatActivity {
    private EditText etCorreo, etContrasena;
    private Button btnIniciarSesion;
    private boolean isPasswordVisible = false; // Controlar visibilidad de la contraseña
    private ProgressDialog progressDialog; // Declaración del ProgressDialog

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);

        // Inicialización del ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

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

            if (correo.isEmpty() || contrasena.isEmpty()) {
                mostrarErrores("Completa todos los campos.");
            } else {
                // Mostrar el ProgressDialog al iniciar sesión
                progressDialog.show();

                loginViewModel.login(correo, contrasena);
                loginViewModel.getLoginMessage().observe(this, message -> {
                    // Ocultar el ProgressDialog cuando se recibe respuesta
                    progressDialog.dismiss();

                    if ("REDIRECT".equals(message)) {
                        Intent intent = new Intent(this, Home.class);
                        startActivity(intent);
                        finish();
                    } else if ("VERIFY_CODE".equals(message)) {
                        Intent intent = new Intent(this, code_verify.class);
                        intent.putExtra("email", etCorreo.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    } else if (message != null) {
                        mostrarErrores(message);
                    }
                });
            }
        });

        TextView crearCuentaTextView = findViewById(R.id.tv_crear_cuenta);
        crearCuentaTextView.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });
    }

    // Método para mostrar errores
    private void mostrarErrores(String mensaje) {
        TextView tvLoginErrors = findViewById(R.id.tv_login_errors);
        tvLoginErrors.setVisibility(View.VISIBLE);
        tvLoginErrors.setText(mensaje);
    }
}
