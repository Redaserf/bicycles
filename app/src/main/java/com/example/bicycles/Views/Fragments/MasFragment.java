package com.example.bicycles.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bicycles.R;
import com.example.bicycles.Views.IniciarRecorridoActivity;

public class MasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.activity_mas, container, false);

        // Configurar el botón
        Button btnIniciarRecorrido = view.findViewById(R.id.btn_iniciar_recorrido);
        btnIniciarRecorrido.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), IniciarRecorridoActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
