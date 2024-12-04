package com.example.bicycles.Views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bicycles.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername, etLastname, etEmail;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inicializar vistas
        etUsername = findViewById(R.id.et_username);
        etLastname = findViewById(R.id.et_lastname);
        etEmail = findViewById(R.id.et_email);
        btnSave = findViewById(R.id.btn_save);

        // Simulación de carga de datos iniciales (puedes cambiar esto según tu implementación)
        etUsername.setText("Juan");
        etLastname.setText("Pérez");
        etEmail.setText("juan.perez@example.com");

        // Configurar botón Guardar
        btnSave.setOnClickListener(view -> {
            // Obtener datos actualizados
            String updatedUsername = etUsername.getText().toString().trim();
            String updatedLastname = etLastname.getText().toString().trim();
            String updatedEmail = etEmail.getText().toString().trim();

            // Aquí puedes realizar la lógica que necesites, como enviar los datos al servidor
            // o almacenarlos en una base de datos local.

            // Por ejemplo:
            System.out.println("Nombre: " + updatedUsername);
            System.out.println("Apellido: " + updatedLastname);
            System.out.println("Correo: " + updatedEmail);

            // Finalizar la actividad
            finish();
        });
    }
}
