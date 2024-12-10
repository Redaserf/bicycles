package com.example.bicycles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.ViewModels.ReenviarCodeViewModel;
import com.example.bicycles.ViewModels.VerifyCodeViewModel;
import com.example.bicycles.Views.Home;

public class code_verify extends AppCompatActivity {

    private EditText etCode;
    private Button btnVerify;
    private TextView tvVerifyErrors, tvReenvio, tvReesendCode;
    private VerifyCodeViewModel verifyCodeViewModel;
    private ReenviarCodeViewModel reenviarCodeViewModel;
    private String email;
    private ProgressDialog progressDialog; // Declaración del ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);

        etCode = findViewById(R.id.et_code);
        btnVerify = findViewById(R.id.btn_verify);
        tvVerifyErrors = findViewById(R.id.tv_login_errors);
        tvReenvio = findViewById(R.id.tv_reenvio);
        tvReesendCode = findViewById(R.id.Tv_reesend_code);

        // Inicialización del ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor, espera...");
        progressDialog.setCancelable(false);

        Factory factory = new Factory(this);
        verifyCodeViewModel = new ViewModelProvider(this, factory).get(VerifyCodeViewModel.class);
        reenviarCodeViewModel = new ViewModelProvider(this, factory).get(ReenviarCodeViewModel.class);

        email = getIntent().getStringExtra("email");

        btnVerify.setOnClickListener(v -> verificarCodigo());
        tvReesendCode.setOnClickListener(v -> reenviarCodigo());
    }

    private void verificarCodigo() {
        String code = etCode.getText().toString().trim();

        if (code.isEmpty()) {
            mostrarErrores(tvVerifyErrors, "Por favor, ingresa el código de verificación.");
            return;
        }

        // Mostrar ProgressDialog antes de enviar el código
        progressDialog.setMessage("Verificando código...");
        progressDialog.show();

        verifyCodeViewModel.verifyCode(email, code);
        observarVerificacion();
    }

    private void reenviarCodigo() {
        // Mostrar ProgressDialog antes de reenviar el código
        progressDialog.setMessage("Reenviando código...");
        progressDialog.show();

        reenviarCodeViewModel.reenviarCodigo(email);
        observarReenvio();
    }

    private void observarVerificacion() {
        verifyCodeViewModel.getVerifyResponse().observe(this, response -> {
            // Ocultar ProgressDialog al recibir respuesta
            progressDialog.dismiss();

            if ("VERIFICADO".equals(response)) {
                Toast.makeText(this, "Cuenta verificada con éxito.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
            } else {
                mostrarErrores(tvVerifyErrors, response);
            }
        });
    }

    private void observarReenvio() {
        reenviarCodeViewModel.getReenviarResponse().observe(this, mensaje -> {
            // Ocultar ProgressDialog al recibir respuesta
            progressDialog.dismiss();

            if (mensaje != null) {
                tvReenvio.setVisibility(View.VISIBLE);
                tvReenvio.setText(mensaje);
            } else {
                tvReenvio.setVisibility(View.GONE);
            }
        });
    }

    private void mostrarErrores(TextView textView, String mensaje) {
        Log.d("MensajeReenvio", mensaje);
        textView.setVisibility(View.VISIBLE);
        textView.setText(mensaje);
    }
}
