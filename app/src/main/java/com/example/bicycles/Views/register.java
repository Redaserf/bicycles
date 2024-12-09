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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.RegisterViewModel;
import com.example.bicycles.code_verify;

public class register extends AppCompatActivity {
    private EditText etName, etLastName, etPeso, etEmail, etPassword;
    private TextView tvRegisterErrors;
    private Button btnRegister;
    private RegisterViewModel registerViewModel;
    private boolean isPasswordVisible = false; // Controlar visibilidad de la contraseña
    private ProgressDialog progressDialog; // Declarar el ProgressDialog

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        etName = findViewById(R.id.et_nombre);
        etLastName = findViewById(R.id.et_apellidos);
        etPeso = findViewById(R.id.et_peso);
        etEmail = findViewById(R.id.et_correo);
        etPassword = findViewById(R.id.et_contrasena);
        btnRegister = findViewById(R.id.btn_registrarse);
        tvRegisterErrors = findViewById(R.id.tv_register_errors);

        // Inicializar el ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false);

        // Configurar funcionalidad del ícono del ojo para la contraseña
        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Detectar si se tocó el ícono del ojo (en el lado derecho)
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;

                    if (isPasswordVisible) {
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24, // Ícono de candado
                                0,
                                R.drawable.baseline_visibility_24, // Ícono de ojo abierto
                                0
                        );
                    } else {
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24, // Ícono de candado
                                0,
                                R.drawable.baseline_visibility_off_24, // Ícono de ojo cerrado
                                0
                        );
                    }

                    // Mover cursor al final del texto
                    etPassword.setSelection(etPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        // Configurar ViewModel
        Factory factory = new Factory(this);
        registerViewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String pesoStr = etPeso.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || lastName.isEmpty() || pesoStr.isEmpty() || email.isEmpty() || password.isEmpty()) {
                mostrarErrores("Completa todos los campos.");
                return;
            }

            Double peso;
            try {
                peso = Double.parseDouble(pesoStr);
            } catch (NumberFormatException e) {
                mostrarErrores("Peso no válido");
                return;
            }

            // Mostrar el ProgressDialog antes de iniciar el registro
            progressDialog.show();

            // Registrar usuario
            registerViewModel.register(name, lastName, peso, email, password);
            observeRegisterResponse(email, password);
        });
    }

    private void observeRegisterResponse(String email, String password) {
        registerViewModel.getRegisterMessage().observe(this, message -> {
            // Ocultar el ProgressDialog al recibir la respuesta
            progressDialog.dismiss();

            if ("Registro exitoso".equals(message)) {
                // Redirigir a la vista de verificación
                Intent intent = new Intent(register.this, code_verify.class);
                intent.putExtra("email", email); // Pasar el email al siguiente activity
                startActivity(intent);
                finish();
            } else if (message != null) {
                mostrarErrores(message);
            }
        });
    }

    private void autoLogin(String email, String password) {
        registerViewModel.login(email, password);
        registerViewModel.getLoginMessage().observe(this, loginMessage -> {
            if ("REDIRECT".equals(loginMessage)) {
                Intent intent = new Intent(register.this, Home.class);
                startActivity(intent);
                finish();
            } else if (loginMessage != null) {
                mostrarErrores(loginMessage);
            }
        });
    }

    private void mostrarErrores(String mensaje) {
        tvRegisterErrors.setVisibility(View.VISIBLE);
        tvRegisterErrors.setText(mensaje);
    }
}
