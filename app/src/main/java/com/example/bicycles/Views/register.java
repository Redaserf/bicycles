package com.example.bicycles.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.RegisterViewModel;

public class register extends AppCompatActivity {
    private EditText etName, etLastName, etPeso, etEmail, etPassword;
    private Button btnRegister;
    private RegisterViewModel registerViewModel;
    private TextView tvRegisterError; // Para mostrar mensajes de error

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
        tvRegisterError = findViewById(R.id.tv_register_error); // TextView para errores

        // Configurar ViewModel
        Factory factory = new Factory(this);
        registerViewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String pesoStr = etPeso.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validaciones de campos
            if (name.isEmpty() || lastName.isEmpty() || pesoStr.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("Por favor, rellena todos los campos.");
                return;
            }

            // Validar peso como número
            Double peso;
            try {
                peso = Double.parseDouble(pesoStr);
            } catch (NumberFormatException e) {
                showError("El peso debe ser un número válido.");
                return;
            }

            // Registrar usuario
            registerViewModel.register(name, lastName, peso, email, password, this);
            observeRegisterResponse();
        });
    }

    private void observeRegisterResponse() {
        registerViewModel.getRegisterMessage().observe(this, message -> {
            if ("Registro exitoso".equals(message)) {
                // Redirigir al HomeActivity con la indicación de cargar MasFragment
                Intent intent = new Intent(register.this, Home.class);
                intent.putExtra("load_fragment", "mas_fragment"); // Indicar que se debe cargar MasFragment
                startActivity(intent);
                finish(); // Evitar que el usuario regrese al registro
            } else if (message != null) {
                showError(message); // Mostrar mensaje de error del ViewModel
            }
        });
    }

    // Mostrar mensaje de error en el TextView y ocultarlo después de 10 segundos
    private void showError(String errorMessage) {
        tvRegisterError.setText(errorMessage);
        tvRegisterError.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> tvRegisterError.setVisibility(View.GONE), 5000);
    }
}
