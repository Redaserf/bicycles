package com.example.bicycles.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bicycles.R;
import com.example.bicycles.Views.PrivacyPolicyActivity;

public class ConfiguracionFragment extends Fragment {

    private Button btnPrivacyPolicy, btnSupport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_configuracion, container, false);

        // Inicializar los botones
        btnPrivacyPolicy = view.findViewById(R.id.btn_privacy_policy);
        btnSupport = view.findViewById(R.id.btn_support);

        // Configurar los listeners
        btnPrivacyPolicy.setOnClickListener(v -> mostrarPoliticaPrivacidad());
        btnSupport.setOnClickListener(v -> mostrarSoporte());

        return view;
    }

    private void mostrarPoliticaPrivacidad() {
        // Abre una actividad que muestra la política de privacidad
        Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    private void mostrarSoporte() {
        // Enviar un correo de soporte
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bicyclesutt@gmail.com"}); // Correo válido
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Soporte Técnico");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola, necesito ayuda con...");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo usando:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No hay clientes de correo instalados.", Toast.LENGTH_SHORT).show();
        }
    }
}