package com.example.bicycles.Views.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Adapters.RecorridosAdapter;
import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;
import com.example.bicycles.ViewModels.AllRecorridosUsuarioViewModel;

public class MisRecorridosFragment extends Fragment {

    private static final int FILTRO_TODOS = 1;
    private static final int FILTRO_SEMANA = 2;
    private static final int FILTRO_MES = 3;

    private AllRecorridosUsuarioViewModel allRecorridosUsuarioViewModel;
    private static final String TAG = "MisRecorridosFragment";
    private RecyclerView recyclerView;
    private RecorridosAdapter adapter;
    private Button filtroButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_recorridos, container, false);

        // Configura el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_recorridos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configura el botón de filtro
        filtroButton = view.findViewById(R.id.filtro);
        filtroButton.setOnClickListener(v -> mostrarMenuDeFiltro());

        // Configura el ViewModel
        Factory factory = new Factory(requireContext());
        allRecorridosUsuarioViewModel = new ViewModelProvider(this, factory).get(AllRecorridosUsuarioViewModel.class);

        // Cargar todos los recorridos por defecto
        cargarRecorridos();

        return view;
    }

    private void mostrarMenuDeFiltro() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), filtroButton);
        popupMenu.getMenu().add(0, FILTRO_TODOS, 0, "Todos los recorridos");
        popupMenu.getMenu().add(0, FILTRO_SEMANA, 1, "Última semana");
        popupMenu.getMenu().add(0, FILTRO_MES, 2, "Último mes");

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case FILTRO_TODOS:
                    cargarRecorridos();
                    break;
                case FILTRO_SEMANA:
                    cargarRecorridosSemana();
                    break;
                case FILTRO_MES:
                    cargarRecorridosMes();
                    break;
                default:
                    return false;
            }
            return true;
        });

        popupMenu.show();
    }


    private void cargarRecorridos() {
        allRecorridosUsuarioViewModel.cargarRecorridos();
        observarCambiosDeRecorridos();
    }

    private void cargarRecorridosSemana() {
        allRecorridosUsuarioViewModel.cargarRecorridosSemana();
        observarCambiosDeRecorridos();
    }

    private void cargarRecorridosMes() {
        allRecorridosUsuarioViewModel.cargarRecorridosMes();
        observarCambiosDeRecorridos();
    }

    private void observarCambiosDeRecorridos() {
        allRecorridosUsuarioViewModel.getRecorridosLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRecorridos() != null) {
                Log.d(TAG, "Recorridos obtenidos con éxito: " + response.getRecorridos().size());
                adapter = new RecorridosAdapter(response.getRecorridos());
                recyclerView.setAdapter(adapter);
            } else {
                Log.e(TAG, "Error al obtener los recorridos o no hay datos disponibles.");
            }
        });
    }
}
