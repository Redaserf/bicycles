package com.example.bicycles.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

public class MisBicisFragment extends Fragment {
    public RecyclerView recyclerMisBicis;
    public List<Bicicleta> bicicletas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull
                 LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_bicis, container, false);
        recyclerMisBicis = view.findViewById(R.id.recycler_mis_bicis);

        Factory factory = new Factory(getContext());
        MisBicisViewModel misBicisViewModel = new ViewModelProvider(
                this, factory).get(MisBicisViewModel.class);

        misBicisViewModel.fetchMisBicicletas();

        misBicisViewModel.getMisBicicletas().observe(getViewLifecycleOwner(), new Observer<List<Bicicleta>>() {
            @Override
            public void onChanged(List<Bicicleta> bicicletas) {
                MisBicisAdapter adapter = new MisBicisAdapter(bicicletas);
                recyclerMisBicis.setAdapter(adapter);
                recyclerMisBicis.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerMisBicis.setHasFixedSize(true);
            }
        });



        return view;
    }
}
