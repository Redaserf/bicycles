package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Usuario;
import com.example.bicycles.R;
import com.example.bicycles.Responses.RegisterResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.ViewModels.RegisterViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register extends AppCompatActivity {
    private EditText etName, etLastName, etEmail, etPassword;
    private Button btnRegister;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
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
        // Listener para alternar la visibilidad de la contraseña
        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Detectar si se tocó el ícono del "ojito"
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;

                    if (isPasswordVisible) {
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24,
                                0,
                                R.drawable.baseline_visibility_24, // Ícono ojito abierto
                                0
                        );
                    } else {
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_lock_24,
                                0,
                                R.drawable.baseline_visibility_off_24, // Ícono ojito cerrado
                                0
                        );
                    }
//                    // Mover cursor al final del texto
//                    etPassword.setSelection(etPassword.getText().length());
                    return true;
                }
            }
            return false;
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Factory factory = new Factory(this);
        RegisterViewModel registerViewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);
//
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                registerViewModel.register(name, lastName, email, password, register.this);
//                De este y el login falta el observe de la LiveData del viewModel
//                 y que al cambiar chequen el valor y pongan una alerta con el texto de la liveData o algo asi
                registrarUsuario(name, lastName, email, password);
            }
        });
    }


    private void registrarUsuario(String name, String lastName, String email, String password) {
        Usuario usuario = new Usuario(name, lastName, email, password);

        RetrofitClient.getInstance(register.this).getApiService().register(usuario).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(register.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(register.this, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(register.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
