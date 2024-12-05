package com.example.bicycles.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.RegisterViewModel;

public class register extends AppCompatActivity {
    private EditText etName, etLastName, etPeso, etEmail, etPassword;
    private Button btnRegister;
    private RegisterViewModel registerViewModel;

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
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Double peso;
            try {
                peso = Double.parseDouble(pesoStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Peso no válido", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(register.this, Home.class);
                startActivity(intent);
                finish(); // Evitar que el usuario regrese al registro
            } else if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
