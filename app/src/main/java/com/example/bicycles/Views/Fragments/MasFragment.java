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
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

public class MasFragment extends Fragment implements OnBicicletaClickListener {
    private static final String CHANNEL_ID = "recorrido_channel";
    private NotificationManager notificationManager;
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
    private RecyclerView recyclerView;
    private MisBicisDialogAdapter adapter;
    private List<Bicicleta> bicicletas = new ArrayList<>();

    private SearchView buscar;
    private View dialogView;
    private AlertDialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        // Crear el canal de notificaciones
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

        Runnable notificationRunnable = new Runnable() {
            @Override
            public void run() {
                String tiempoActual = tiempoTranscurrido.getText().toString();

                Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                        .setContentTitle("Recorrido en progreso")
                        .setContentText("Tiempo transcurrido: " + tiempoActual)
                        .setSmallIcon(R.drawable.mis_bicis) // Cambia por tu icono
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
    }

    private void iniciarRecorrido(int bicicletaId) {
        recorridoInicioViewModel.iniciarRecorrido(bicicletaId).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                Log.d("DEBUG", "Recorrido iniciado: ID " + response.getRecorridoId());
                recorridoId = response.getRecorridoId();
                iniciarActualizacionesPeriodicas(recorridoId);
                iniciarTemporizador();
                iniciarNotificacion(recorridoId); // Mostrar notificación

                if (listener != null) {
                    listener.onHideBottomNavigation();
                }
            } else {
                Toast.makeText(requireContext(), "Error al iniciar recorrido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void detenerRecorrido() {
        detenerActualizacionesPeriodicas();
        detenerTemporizador();
        detenerNotificacion(); // Detener notificación
        eliminarVelocidadesDelRecorrido();
        reiniciarTextViews();
        isPlaying = false;
        playPauseButton.setImageResource(R.drawable.ic_play);

        if (listener != null) {
            listener.onShowBottomNavigation();
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

        LayoutInflater inflaterLyt = LayoutInflater.from(requireContext());
        dialogView = inflaterLyt.inflate(R.layout.dialog_bicycle_selection, null);
        buscar = dialogView.findViewById(R.id.edTxtBuscar);
        SearchView buscar = dialogView.findViewById(R.id.edTxtBuscar);
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
        misBicisViewModel.getBicicletasLiveData().observe(getViewLifecycleOwner(), new Observer<MisBicicletasResponse>() {
            @Override
            public void onChanged(MisBicicletasResponse misBicicletasResponse) {
                if(misBicicletasResponse.getBicicletas() != null){
                    bicicletas = misBicicletasResponse.getBicicletas();
                    adapter.actualizarLista(misBicicletasResponse.getBicicletas());
                    Log.d("DEBUG", "Se actualizo el adapter de las bicicletas dialog");
                }else{
                    Toast.makeText(requireContext(), "No hay bicicletas para los recorridos", Toast.LENGTH_LONG).show();
                }
                Log.d("DEBUG", "Cantidad de bicics: " + misBicicletasResponse.getBicicletas().size());
            }
        });
//        misBicisViewModel.fetchBicicletas();

        return view;
    }

    private void showBicycleSelectionDialog() {
        // Mostrar el diálogo (datos ya actualizados por el LiveData)

        misBicisViewModel.fetchBicicletas();
        dialog.show();
    }



    private void mostrarAlertaDetenerRecorrido() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Recorrido en progreso")
                .setMessage("¿Desea detener el recorrido?")
                .setPositiveButton("Sí", (dialog, which) -> detenerRecorrido())
                .setNegativeButton("No", null)
                .show();
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

    @Override
    public void onBicicletaClick(Bicicleta bicicleta) {
        Log.d("DEBUG", "Bicicleta seleccionada: " + bicicleta.getNombre());
        bicicletaSeleccionadaId = (long) bicicleta.getId();
        playPauseButton.setImageResource(R.drawable.ic_pause);
        isPlaying = true;
        iniciarRecorrido(bicicleta.getId());
        dialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        void onHideBottomNavigation();

        void onShowBottomNavigation();
    }
}