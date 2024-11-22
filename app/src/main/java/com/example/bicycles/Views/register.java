package com.example.bicycles.Views;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.R;
import com.example.bicycles.ViewModels.RegisterViewModel;

public class register extends AppCompatActivity {
    private EditText etName, etLastName, etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        etName = findViewById(R.id.et_nombre);
        etLastName = findViewById(R.id.et_apellidos);
        etEmail = findViewById(R.id.et_correo);
        etPassword = findViewById(R.id.et_contrasena);
        btnRegister = findViewById(R.id.btn_registrarse);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RegisterViewModel registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                registerViewModel.register(name, lastName, email, password, register.this);
                //De este y el login falta el observe de la LiveData del viewModel
                // y que al cambiar chequen el valor y pongan una alerta con el texto de la liveData o algo asi
//                registrarUsuario(name, lastName, email, password);
            }
        });
    }


//    private void registrarUsuario(String name, String lastName, String email, String password) {
//        RegisterRequest request = new RegisterRequest(name, lastName, email, password);
//
//        RetrofitClient.getInstance().getApiService().register(request).enqueue(new Callback<RegisterResponse>() {
//            @Override
//            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(register.this, "Error en el registro", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterResponse> call, Throwable t) {
//                Toast.makeText(register.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
