package com.example.bicycles.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Adapters.MisBicisAdapter;
import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.MisBicisViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MisBicisFragment extends Fragment {
//    private RecyclerView recyclerMisBicis;
//    private List<Bicicleta> bicicletas = new ArrayList<>();
//    private Button btnToggleOrder; // Botón para alternar orden
//    private boolean isAscending = true; // Bandera para saber si el orden es ascendente o descendente
//    private MisBicisAdapter adapter;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_mis_bicis, container, false);
//
//        // Configurar RecyclerView y botón
//        recyclerMisBicis = view.findViewById(R.id.recycler_mis_bicis);
//        btnToggleOrder = view.findViewById(R.id.btn_toggle_order);
//
//        Factory factory = new Factory(getContext());
//        MisBicisViewModel misBicisViewModel = new ViewModelProvider(this, factory).get(MisBicisViewModel.class);
//
//        // Obtener bicicletas desde el ViewModel
//        misBicisViewModel.fetchMisBicicletas();
//
//        // Observer para recibir la lista de bicicletas
//        misBicisViewModel.getMisBicicletas().observe(getViewLifecycleOwner(), new Observer<List<Bicicleta>>() {
//            @Override
//            public void onChanged(List<Bicicleta> bicicletasList) {
//                bicicletas = new ArrayList<>(bicicletasList); // Clonar la lista para ordenarla
//                updateRecyclerView();
//            }
//        });
//
//        // Configurar botón para alternar entre orden ascendente y descendente
//        btnToggleOrder.setOnClickListener(v -> {
//            isAscending = !isAscending; // Alternar bandera
//            updateRecyclerView(); // Actualizar RecyclerView con el nuevo orden
//        });
//
//        return view;
//    }
//
//    /**
//     * Actualiza el RecyclerView con la lista ordenada según el estado de la bandera isAscending.
//     */
//    private void updateRecyclerView() {
//        // Ordenar la lista según la bandera
//        if (isAscending) {
//            Collections.sort(bicicletas, Comparator.comparingInt(Bicicleta::getId)); // Ascendente
//            btnToggleOrder.setText("Ordenar Descendente");
//        } else {
//            Collections.sort(bicicletas, (b1, b2) -> Integer.compare(b2.getId(), b1.getId())); // Descendente
//            btnToggleOrder.setText("Ordenar Ascendente");
//        }
//
//        // Configurar el adaptador y el RecyclerView
//        adapter = new MisBicisAdapter(bicicletas);
//        recyclerMisBicis.setAdapter(adapter);
//        recyclerMisBicis.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerMisBicis.setHasFixedSize(true);
//    }
}
