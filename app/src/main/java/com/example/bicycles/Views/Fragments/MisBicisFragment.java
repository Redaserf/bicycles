package com.example.bicycles.Views.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.example.bicycles.Views.Agregar_bici;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MisBicisFragment extends Fragment {
    public RecyclerView recyclerMisBicis;
    public List<Bicicleta> bicicletas = new ArrayList<>();
    public Button agregar_bici;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_bicis, container, false);
        agregar_bici = view.findViewById(R.id.agregar_bici);

        agregar_bici.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Agregar_bici.class);
            startActivity(intent);
        });

        recyclerMisBicis = view.findViewById(R.id.recycler_mis_bicis);
        Context context = requireContext();
        Factory factory = new Factory(context);

        MisBicisViewModel misBicisViewModel = new ViewModelProvider(
                this, factory).get(MisBicisViewModel.class);

        misBicisViewModel.fetchMisBicicletas();
        misBicisViewModel.getMisBicicletas().observe(getViewLifecycleOwner(), bicicletas -> {
            MisBicisAdapter adapter = new MisBicisAdapter(bicicletas, bicicleta -> {
                // Aquí defines qué hacer cuando se selecciona una bicicleta
//                Toast.makeText(getContext(), "Bicicleta seleccionada: " + bicicleta.getNombre(), Toast.LENGTH_SHORT).show();
            });
            recyclerMisBicis.setAdapter(adapter);
            recyclerMisBicis.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerMisBicis.setHasFixedSize(true);
        });

        return view;
    }
}
