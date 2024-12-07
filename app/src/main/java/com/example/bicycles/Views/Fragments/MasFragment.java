package com.example.bicycles.Views.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Adapters.MisBicisDialogAdapter;
import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.EliminarVelocidadViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.ViewModels.RecorridoInicioViewModel;
import com.example.bicycles.ViewModels.SensoresViewModel;

public class MasFragment extends Fragment {

    private boolean isPlaying = false; // Estado inicial para el botón de play/pause
    private MisBicisViewModel misBicisViewModel;
    private RecorridoInicioViewModel recorridoInicioViewModel;
    private SensoresViewModel sensoresViewModel;
    private EliminarVelocidadViewModel eliminarVelocidadViewModel;
    private TextView velocidadActual, velocidadMaxima, velocidadPromedio, distanciaRecorrida, caloriasQuemadas, temperaturaView;
    private Bicicleta bicicletaSeleccionada;
    private Long bicicletaSeleccionadaId;
    private Integer recorridoId;
    private ImageButton playPauseButton;
    private Handler handler = new Handler();
    private Runnable dataFetcher;
    private TextView tiempoTranscurrido;
    private long tiempoInicio = 0;
    private Handler tiempoHandler = new Handler();
    private Runnable tiempoRunnable;
    private OnFragmentInteractionListener listener; // Callback para interactuar con la actividad principal

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mas, container, false);

        // Inicializar vistas
        playPauseButton = view.findViewById(R.id.play_pause_button);
        velocidadActual = view.findViewById(R.id.velocidad_actual);
        velocidadMaxima = view.findViewById(R.id.velocidad_maxima);
        velocidadPromedio = view.findViewById(R.id.velocidad_promedio);
        tiempoTranscurrido = view.findViewById(R.id.tiempo_transcurrido);
        distanciaRecorrida = view.findViewById(R.id.distancia_recorrida);
        caloriasQuemadas = view.findViewById(R.id.tv_calorias);
        temperaturaView = view.findViewById(R.id.tv_temperatura);

        Factory factory = new Factory(requireContext());
        misBicisViewModel = new ViewModelProvider(this, factory).get(MisBicisViewModel.class);
        recorridoInicioViewModel = new ViewModelProvider(this, factory).get(RecorridoInicioViewModel.class);
        sensoresViewModel = new ViewModelProvider(this, factory).get(SensoresViewModel.class);
        eliminarVelocidadViewModel = new ViewModelProvider(this, factory).get(EliminarVelocidadViewModel.class);

        playPauseButton.setOnClickListener(v -> {
            if (!isPlaying) {
                showBicycleSelectionDialog();
            } else {
                mostrarAlertaDetenerRecorrido();
            }
        });

        return view;
    }

    private void showBicycleSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona una bicicleta");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_bicycle_selection, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_bicycles);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        AlertDialog dialog = builder.setView(dialogView)
                .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
                .create();

        misBicisViewModel.fetchMisBicicletas();
        misBicisViewModel.getMisBicicletas().observe(getViewLifecycleOwner(), bicicletas -> {
            if (bicicletas != null && !bicicletas.isEmpty()) {
                MisBicisDialogAdapter adapter = new MisBicisDialogAdapter(bicicletas, bicicleta -> {
                    bicicletaSeleccionadaId = (long) bicicleta.getId();
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    isPlaying = true;
                    iniciarRecorrido(bicicleta.getId());
                    dialog.dismiss();
                });
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(requireContext(), "No tienes bicicletas registradas.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void iniciarRecorrido(int bicicletaId) {
        recorridoInicioViewModel.iniciarRecorrido(bicicletaId).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                recorridoId = response.getRecorridoId();
                iniciarActualizacionesPeriodicas(recorridoId);
                iniciarTemporizador();

                if (listener != null) {
                    listener.onHideBottomNavigation();
                }
            } else {
                Toast.makeText(requireContext(), "Error al iniciar recorrido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarAlertaDetenerRecorrido() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Recorrido en progreso")
                .setMessage("¿Desea detener el recorrido?")
                .setPositiveButton("Sí", (dialog, which) -> detenerRecorrido())
                .setNegativeButton("No", null)
                .show();
    }

    private void detenerRecorrido() {
        detenerActualizacionesPeriodicas();
        detenerTemporizador();
        eliminarVelocidadesDelRecorrido();
        reiniciarTextViews();
        isPlaying = false;
        playPauseButton.setImageResource(R.drawable.ic_play);

        if (listener != null) {
            listener.onShowBottomNavigation();
        }
    }

    private void iniciarActualizacionesPeriodicas(int recorridoId) {
        dataFetcher = new Runnable() {
            @Override
            public void run() {
                String tiempoActual = tiempoTranscurrido.getText().toString();
                sensoresViewModel.fetchSensores(recorridoId, tiempoActual).observe(getViewLifecycleOwner(), sensoresResponse -> {
                    if (sensoresResponse != null && sensoresResponse.isSuccess()) {
                        velocidadActual.setText(String.format("%.2f km/h", sensoresResponse.getVelocidadActual()));
                        velocidadMaxima.setText(String.format("%.2f km/h", sensoresResponse.getVelocidadMaxima()));
                        velocidadPromedio.setText(String.format("%.2f km/h", sensoresResponse.getVelocidadPromedio()));
                        distanciaRecorrida.setText(String.format("%.2f km", sensoresResponse.getDistanciaRecorrida()));
                        caloriasQuemadas.setText(String.format("Calorías quemadas: %.2f kcal", sensoresResponse.getCalorias()));
                        temperaturaView.setText(String.format("Temperatura: %.2f°C", sensoresResponse.getTemperatura()));
                    }
                });

                // Tiempo de las consultas
                handler.postDelayed(this, 3000);
            }
        };

        handler.post(dataFetcher);
    }

    private void detenerActualizacionesPeriodicas() {
        if (dataFetcher != null) {
            handler.removeCallbacks(dataFetcher);
        }
    }

    private void detenerTemporizador() {
        tiempoHandler.removeCallbacks(tiempoRunnable);
        tiempoTranscurrido.setText("00:00:00");
    }

    private void eliminarVelocidadesDelRecorrido() {
        if (recorridoId == null) {
            Log.e("EliminarVelocidad", "El recorridoId es nulo. No se puede eliminar velocidades.");
            return;
        }

        eliminarVelocidadViewModel.eliminarVelocidades(recorridoId).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                Log.i("EliminarVelocidad", "Velocidades eliminadas correctamente.");
            } else {
                Toast.makeText(requireContext(), "Error al eliminar las velocidades", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reiniciarTextViews() {
        velocidadActual.setText("0.0 km/h");
        velocidadMaxima.setText("0.0 km/h");
        velocidadPromedio.setText("0.0 km/h");
        distanciaRecorrida.setText("0.0 km");
        caloriasQuemadas.setText("Calorías quemadas: 0.00 kcal");
        temperaturaView.setText("Temperatura: ");
    }

    private void iniciarTemporizador() {
        tiempoInicio = System.currentTimeMillis();
        tiempoRunnable = new Runnable() {
            @Override
            public void run() {
                long tiempoActual = System.currentTimeMillis();
                long tiempoTranscurridoMillis = tiempoActual - tiempoInicio;

                int segundos = (int) (tiempoTranscurridoMillis / 1000) % 60;
                int minutos = (int) ((tiempoTranscurridoMillis / (1000 * 60)) % 60);
                int horas = (int) ((tiempoTranscurridoMillis / (1000 * 60 * 60)) % 24);

                tiempoTranscurrido.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));

                tiempoHandler.postDelayed(this, 1000);
            }
        };
        tiempoHandler.post(tiempoRunnable);
    }

    public interface OnFragmentInteractionListener {
        void onHideBottomNavigation();

        void onShowBottomNavigation();
    }
}
