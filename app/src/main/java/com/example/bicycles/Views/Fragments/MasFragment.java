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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Adapters.MisBicisDialogAdapter;
import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.ViewModels.EliminarVelocidadViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.ViewModels.RecorridoInicioViewModel;
import com.example.bicycles.ViewModels.SensoresViewModel;
import com.example.bicycles.Views.OnBicicletaClickListener;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationChannel;


import java.util.ArrayList;
import java.util.List;

public class MasFragment extends Fragment implements OnBicicletaClickListener {
    private static final String CHANNEL_ID = "recorrido_channel";
    private NotificationManager notificationManager;
    private Runnable notificationRunnable;
    private boolean isNotificationActive = false; // Indicador de notificación activa
    private boolean isPlaying = false;
    private boolean isPaused = false; // Indica si el recorrido está en pausa
    private MisBicisViewModel misBicisViewModel;
    private RecorridoInicioViewModel recorridoInicioViewModel;
    private SensoresViewModel sensoresViewModel;
    private EliminarVelocidadViewModel eliminarVelocidadViewModel;
    private TextView velocidadActual, velocidadMaxima, velocidadPromedio, distanciaRecorrida, caloriasQuemadas, temperaturaView;
    private Bicicleta bicicletaSeleccionada;
    private Long bicicletaSeleccionadaId;
    private Integer recorridoId;
    private ImageButton playPauseButton, pauseButton; // Botón de pausa
    private Handler handler = new Handler();
    private Runnable dataFetcher;
    private TextView tiempoTranscurrido;
    private long tiempoInicio = 0;
    private long tiempoPausado = 0;
    private Handler tiempoHandler = new Handler();
    private Runnable tiempoRunnable;
    private OnFragmentInteractionListener listener;
    private RecyclerView recyclerView;
    private MisBicisDialogAdapter adapter;
    private List<Bicicleta> bicicletas = new ArrayList<>();
    private SearchView buscar;
    private View dialogView;
    private AlertDialog dialog;
    private TextView tvPausado;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        createNotificationChannel();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificaciones de Recorrido",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Notificaciones del estado del recorrido");

            notificationManager = requireContext().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void iniciarNotificacion(int recorridoId) {
        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        isNotificationActive = true;

        notificationRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isNotificationActive) return; // Si se detuvo, no actualizar

                String tiempoActual = tiempoTranscurrido.getText().toString();

                Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                        .setContentTitle("Recorrido en progreso")
                        .setContentText("Tiempo transcurrido: " + tiempoActual)
                        .setSmallIcon(R.drawable.bicicletatarjeta) // Cambia por tu icono
                        .setOngoing(true)
                        .build();

                notificationManager.notify(recorridoId, notification);

                // Actualizar la notificación cada segundo
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(notificationRunnable);
    }

    private void detenerNotificacion() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        isNotificationActive = false;
        if (notificationRunnable != null) {
            handler.removeCallbacks(notificationRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detenerNotificacion();
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
        pauseButton = view.findViewById(R.id.pause_button); // Botón de pausa/reanudar
        velocidadActual = view.findViewById(R.id.velocidad_actual);
        velocidadMaxima = view.findViewById(R.id.velocidad_maxima);
        velocidadPromedio = view.findViewById(R.id.velocidad_promedio);
        tiempoTranscurrido = view.findViewById(R.id.tiempo_transcurrido);
        distanciaRecorrida = view.findViewById(R.id.distancia_recorrida);
        caloriasQuemadas = view.findViewById(R.id.tv_calorias);
        temperaturaView = view.findViewById(R.id.tv_temperatura);
        tvPausado = view.findViewById(R.id.tv_pausado);

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

        pauseButton.setOnClickListener(v -> togglePauseResumeRecorrido()); // Configurar botón de pausa/reanudar

        LayoutInflater inflaterLyt = LayoutInflater.from(requireContext());
        dialogView = inflaterLyt.inflate(R.layout.dialog_bicycle_selection, null);
        buscar = dialogView.findViewById(R.id.edTxtBuscar);
        ImageView searchIcon = buscar.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setVisibility(View.GONE);
        buscar.setIconified(false);

        buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        buscar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Retornar true significa que se consume el evento y NO se cierra
                // Si quieres que la "X" solo limpie el texto, hazlo aquí manualmente
                buscar.setQuery("", false);
                return true;
            }
        });

        recyclerView = dialogView.findViewById(R.id.recycler_bicycles);
        adapter = new MisBicisDialogAdapter(bicicletas, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona una bicicleta");
        builder.setView(dialogView)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();
        misBicisViewModel.getBicicletasLiveData().observe(getViewLifecycleOwner(), misBicicletasResponse -> {
            if (misBicicletasResponse.getBicicletas() != null) {
                bicicletas = misBicicletasResponse.getBicicletas();
                adapter.actualizarLista(misBicicletasResponse.getBicicletas());
                Log.d("DEBUG", "Se actualizó el adapter de las bicicletas dialog");
            } else {
                Toast.makeText(requireContext(), "No hay bicicletas para los recorridos", Toast.LENGTH_LONG).show();
            }
            Log.d("DEBUG", "Cantidad de bicis: " + misBicicletasResponse.getBicicletas().size());
        });

        return view;
    }

    private void togglePauseResumeRecorrido() {
        if (isPaused) {
            // Reanudar el recorrido
            Log.d("DEBUG", "Reanudando recorrido...");
            tiempoInicio = System.currentTimeMillis() - tiempoPausado; // Ajustar tiempo de inicio correctamente
            iniciarActualizacionesPeriodicas(recorridoId);
            iniciarTemporizador();
            pauseButton.setImageResource(R.drawable.ic_pause); // Cambiar icono a pausa
            tvPausado.setVisibility(View.GONE); // Ocultar el texto de "Pausado..."
            isPaused = false;
        } else {
            // Pausar el recorrido
            Log.d("DEBUG", "Pausando recorrido...");
            detenerActualizacionesPeriodicas();
            detenerTemporizador();
            tiempoPausado = System.currentTimeMillis() - tiempoInicio; // Almacenar tiempo pausado
            pauseButton.setImageResource(R.drawable.ic_play); // Cambiar icono a reanudar
            tvPausado.setVisibility(View.VISIBLE); // Mostrar el texto de "Pausado..."
            isPaused = true;
        }
    }

    private void showBicycleSelectionDialog() {
        misBicisViewModel.fetchBicicletas();
        dialog.show();
    }

    private void iniciarRecorrido(int bicicletaId) {
        recorridoInicioViewModel.iniciarRecorrido(bicicletaId).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                recorridoId = response.getRecorridoId();
                tiempoInicio = System.currentTimeMillis();
                iniciarActualizacionesPeriodicas(recorridoId);
                iniciarTemporizador();
                iniciarNotificacion(recorridoId);

                TextView mensajeVelocidad = getView().findViewById(R.id.textoVelocidad);
                mensajeVelocidad.setVisibility(View.VISIBLE);

                pauseButton.setVisibility(View.VISIBLE);
                isPaused = false;

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

        // Ocultar el mensaje de velocidad
        TextView mensajeVelocidad = getView().findViewById(R.id.textoVelocidad);
        mensajeVelocidad.setVisibility(View.GONE);

        playPauseButton.setImageResource(R.drawable.ic_play);
        pauseButton.setVisibility(View.GONE);
        tvPausado.setVisibility(View.GONE);
        isPaused = false;

        tiempoInicio = 0;
        tiempoPausado = 0;

        if (isAdded()) {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MasFragment())
                    .commit();
        } else {
            Log.e("MasFragment", "Fragmento no está adjunto. No se puede reemplazar.");
        }

        detenerNotificacion();

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
                        double velocidadActualValor = sensoresResponse.getVelocidadActual();

                        // Actualizar las vistas con los datos del sensor
                        velocidadActual.setText(String.format("%.2f km/h", velocidadActualValor));
                        velocidadMaxima.setText(String.format("%.2f km/h", sensoresResponse.getVelocidadMaxima()));
                        velocidadPromedio.setText(String.format("%.2f km/h", sensoresResponse.getVelocidadPromedio()));
                        distanciaRecorrida.setText(String.format("%.2f km", sensoresResponse.getDistanciaRecorrida()));
                        caloriasQuemadas.setText(String.format("Calorías quemadas: %.2f kcal", sensoresResponse.getCalorias()));
                        temperaturaView.setText(String.format("Temperatura: %.2f°C", sensoresResponse.getTemperatura()));

                        // Llamar al método para actualizar el texto y el color según la velocidad
                        actualizarTextoVelocidad(velocidadActualValor);
                    }
                });

                // Handler para actualizar los datos periódicamente
                handler.postDelayed(this, 1800); // Actualización cada 1.8 segundos
            }
        };

        handler.post(dataFetcher);
    }

    private void actualizarTextoVelocidad(double velocidad) {
        TextView mensajeVelocidad = getView().findViewById(R.id.textoVelocidad);

        if (velocidad <= 10) {
            mensajeVelocidad.setText("Velocidad baja - Estás en un ritmo estable.");
            mensajeVelocidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
        } else if (velocidad <= 18) {
            mensajeVelocidad.setText("Velocidad moderada - Buen ritmo.");
            mensajeVelocidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
        } else {
            mensajeVelocidad.setText("Velocidad alta - Ten cuidado.");
            mensajeVelocidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.rojo));
        }
    }


    private void detenerActualizacionesPeriodicas() {
        if (dataFetcher != null) {
            handler.removeCallbacks(dataFetcher);
        }
    }

    private void detenerTemporizador() {
        tiempoHandler.removeCallbacks(tiempoRunnable);
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
        tiempoRunnable = new Runnable() {
            @Override
            public void run() {
                long tiempoActual = System.currentTimeMillis();
                long tiempoTranscurridoMillis = tiempoActual - tiempoInicio;

                if (isPaused) {
                    tiempoHandler.removeCallbacks(this);
                    return;
                }

                int segundos = (int) (tiempoTranscurridoMillis / 1000) % 60;
                int minutos = (int) ((tiempoTranscurridoMillis / (1000 * 60)) % 60);
                int horas = (int) ((tiempoTranscurridoMillis / (1000 * 60 * 60)) % 24);

                tiempoTranscurrido.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));

                tiempoHandler.postDelayed(this, 1000);
            }
        };
        tiempoHandler.post(tiempoRunnable);
    }

    @Override
    public void onBicicletaClick(Bicicleta bicicleta) {
        Log.d("DEBUG", "Bicicleta seleccionada: " + bicicleta.getNombre());
        bicicletaSeleccionadaId = (long) bicicleta.getId();
        playPauseButton.setImageResource(R.drawable.baseline_stop_24);
        isPlaying = true;
        iniciarRecorrido(bicicleta.getId());
        dialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        void onHideBottomNavigation();

        void onShowBottomNavigation();
    }
}