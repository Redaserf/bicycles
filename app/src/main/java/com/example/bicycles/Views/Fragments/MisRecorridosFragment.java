package com.example.bicycles.Views.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Adapters.RecorridosAdapter;
import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.AllRecorridosUsuarioViewModel;

public class MisRecorridosFragment extends Fragment {

    private static final int FILTRO_TODOS = 1;
    private static final int FILTRO_SEMANA = 2;
    private static final int FILTRO_MES = 3;

    private AllRecorridosUsuarioViewModel allRecorridosUsuarioViewModel;
    private RecyclerView recyclerView;
    private RecorridosAdapter adapter;
    private Button filtroButton;
    private SearchView buscar;
    private ProgressDialog progressDialogCarga; // ProgressDialog para la carga de recorridos
    private static final String TAG = "MisRecorridosFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_recorridos, container, false);

        recyclerView = view.findViewById(R.id.recycler_recorridos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        filtroButton = view.findViewById(R.id.filtro);
        buscar = view.findViewById(R.id.search_view);

        progressDialogCarga = new ProgressDialog(requireContext());
        progressDialogCarga.setMessage("Cargando recorridos...");
        progressDialogCarga.setCancelable(false);

        Factory factory = new Factory(requireContext());
        allRecorridosUsuarioViewModel = new ViewModelProvider(this, factory).get(AllRecorridosUsuarioViewModel.class);

        cargarRecorridos();
        observarCambiosDeRecorridos();

        filtroButton.setOnClickListener(v -> mostrarMenuDeFiltro());
        ImageView searchIcon = buscar.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setVisibility(View.GONE);
        buscar.setIconified(false);

        buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filter(newText);
                }
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
        progressDialogCarga.show();
        allRecorridosUsuarioViewModel.cargarRecorridos();
    }

    private void cargarRecorridosSemana() {
        progressDialogCarga.show();
        allRecorridosUsuarioViewModel.cargarRecorridosSemana();
    }

    private void cargarRecorridosMes() {
        progressDialogCarga.show();
        allRecorridosUsuarioViewModel.cargarRecorridosMes();
    }

    private void observarCambiosDeRecorridos() {
        allRecorridosUsuarioViewModel.getRecorridosLiveData().observe(getViewLifecycleOwner(), response -> {
            progressDialogCarga.dismiss();

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
