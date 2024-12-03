package com.example.bicycles.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bicycles.R;

public class MasFragment extends Fragment {

    private boolean isPlaying = false; // Estado inicial para el botón de play/pause

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.activity_mas, container, false);

        // Configuración del botón Play/Pause
        ImageButton playPauseButton = view.findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(v -> {
            isPlaying = !isPlaying; // Alternar estado

            if (isPlaying) {
                playPauseButton.setImageResource(R.drawable.ic_pause); // Cambiar a pausa
                // Lógica adicional para iniciar el recorrido
            } else {
                playPauseButton.setImageResource(R.drawable.ic_play); // Cambiar a reproducción
                // Lógica adicional para pausar el recorrido
            }
        });

        // Configuración de estadísticas (opcional)
        TextView velocidadActual = view.findViewById(R.id.velocidad_actual);
        velocidadActual.setText("0.0 km/h");

        return view;
    }
}
