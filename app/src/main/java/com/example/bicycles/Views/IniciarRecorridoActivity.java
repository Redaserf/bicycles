package com.example.bicycles.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bicycles.R;

/*public class IniciarRecorridoActivity extends AppCompatActivity {

    private boolean isPlaying = false; // Estado inicial: no está reproduciendo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_recorrido);

        // Botón para volver a la página principal
        Button btnBackToHome = findViewById(R.id.btn_back_to_home);
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(IniciarRecorridoActivity.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Botón estilo Spotify
        ImageButton playPauseButton = findViewById(R.id.play_pause_button);
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
    }
}

 */