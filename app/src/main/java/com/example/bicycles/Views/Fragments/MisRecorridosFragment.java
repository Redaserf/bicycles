package com.example.bicycles.Views.Fragments;

import android.os.Bundle;
import android.util.Log; // Importa la clase Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;
import com.example.bicycles.ViewModels.AllRecorridosUsuarioViewModel;

public class MisRecorridosFragment extends Fragment {

    private AllRecorridosUsuarioViewModel allRecorridosUsuarioViewModel;
    private static final String TAG = "MisRecorridosFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_recorridos, container, false);

        // Configura el ViewModel
        Factory factory = new Factory(requireContext());
        allRecorridosUsuarioViewModel = new ViewModelProvider(this, factory).get(AllRecorridosUsuarioViewModel.class);

        // Observar los datos de los recorridos
        observarRecorridos();

        return view;
    }

    private void observarRecorridos() {
        allRecorridosUsuarioViewModel.cargarRecorridos();

        allRecorridosUsuarioViewModel.getRecorridosLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRecorridos() != null) {
                Log.d(TAG, "Recorridos obtenidos con éxito: " + response.getRecorridos().size());
                for (AllRecorridosUsuarioResponse.Recorrido recorrido : response.getRecorridos()) {
                    Log.d(TAG, "Recorrido: " +
                            "Bicicleta: " + recorrido.getBicicletaNombre() +
                            ", Calorías: " + recorrido.getCalorias() +
                            ", Tiempo: " + recorrido.getTiempo() +
                            ", Velocidad Promedio: " + recorrido.getVelocidadPromedio() +
                            ", Velocidad Máxima: " + recorrido.getVelocidadMaxima() +
                            ", Distancia Recorrida: " + recorrido.getDistanciaRecorrida() +
                            ", Fecha: " + recorrido.getCreatedAt());
                }
            } else {
                Log.e(TAG, "Error al obtener los recorridos o no hay datos disponibles.");
            }
        });
    }
}
