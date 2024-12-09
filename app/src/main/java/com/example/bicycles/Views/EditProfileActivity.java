package com.example.bicycles.Views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.UsuarioEditViewModel;
import com.example.bicycles.ViewModels.UsuarioViewModel;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etPeso;
    private TextView tvProfileErrors;
    private Button btnGuardar;
    private UsuarioEditViewModel usuarioEditViewModel;
    private UsuarioViewModel usuarioViewModel;
    private ProgressDialog progressDialogCarga; // ProgressDialog para la carga de datos
    private ProgressDialog progressDialogGuardado; // ProgressDialog para el guardado de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNombre = findViewById(R.id.et_username);
        etApellido = findViewById(R.id.et_lastname);
        etCorreo = findViewById(R.id.et_email);
        etPeso = findViewById(R.id.et_peso);
        btnGuardar = findViewById(R.id.btn_save);
        tvProfileErrors = findViewById(R.id.tv_profile_errors); // TextView para mostrar errores

        // Inicialización de los ProgressDialog
        progressDialogCarga = new ProgressDialog(this);
        progressDialogCarga.setMessage("Cargando datos...");
        progressDialogCarga.setCancelable(false);

        progressDialogGuardado = new ProgressDialog(this);
        progressDialogGuardado.setMessage("Guardando cambios...");
        progressDialogGuardado.setCancelable(false);

        Factory factory = new Factory(this);
        usuarioEditViewModel = new ViewModelProvider(this, factory).get(UsuarioEditViewModel.class);
        usuarioViewModel = new ViewModelProvider(this, factory).get(UsuarioViewModel.class);

        cargarDatosUsuario();

        btnGuardar.setOnClickListener(v -> actualizarPerfil());
    }

    private void cargarDatosUsuario() {
        // Mostrar el ProgressDialog para la carga de datos
        progressDialogCarga.show();

        usuarioViewModel.cargarUsuario();

        usuarioViewModel.getUsuarioLiveData().observe(this, usuario -> {
            // Ocultar el ProgressDialog al recibir respuesta
            progressDialogCarga.dismiss();

            if (usuario != null) {
                etNombre.setText(usuario.getNombre());
                etApellido.setText(usuario.getApellido());
                etCorreo.setText(usuario.getEmail());
                etPeso.setText(usuario.getPeso() != 0 ? String.valueOf(usuario.getPeso()) : "");
            } else {
                Toast.makeText(this, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarPerfil() {
        String nuevoNombre = etNombre.getText().toString().trim();
        String nuevoApellido = etApellido.getText().toString().trim();
        String nuevoCorreo = etCorreo.getText().toString().trim();
        String nuevoPesoStr = etPeso.getText().toString().trim();

        Double nuevoPeso = null;
        if (!nuevoPesoStr.isEmpty()) {
            try {
                nuevoPeso = Double.parseDouble(nuevoPesoStr);
            } catch (NumberFormatException e) {
                mostrarErrores("Peso no válido");
                return;
            }
        }

        // Mostrar el ProgressDialog antes de la actualización
        progressDialogGuardado.show();

        usuarioEditViewModel.actualizarPerfil(nuevoNombre, nuevoApellido, nuevoPeso, nuevoCorreo, this);

        usuarioEditViewModel.getResponseMessage().observe(this, mensaje -> {
            // Ocultar el ProgressDialog al recibir respuesta
            progressDialogGuardado.dismiss();

            if ("Perfil actualizado correctamente".equals(mensaje)) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mostrarErrores(mensaje);
            }
        });
    }

    private void mostrarErrores(String mensaje) {
        tvProfileErrors.setVisibility(View.VISIBLE);
        tvProfileErrors.setText(mensaje);
    }
}
