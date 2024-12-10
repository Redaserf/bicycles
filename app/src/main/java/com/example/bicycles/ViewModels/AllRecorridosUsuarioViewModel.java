package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.AllRecorridosUsuarioRepository;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;

public class AllRecorridosUsuarioViewModel extends ViewModel {
    private AllRecorridosUsuarioRepository recorridosRepository;
    private MutableLiveData<AllRecorridosUsuarioResponse> recorridosLiveData;

    public AllRecorridosUsuarioViewModel(Context context) {
        recorridosRepository = new AllRecorridosUsuarioRepository(context);
        recorridosLiveData = new MutableLiveData<>(); // Inicializado una vez
    }

    // Cargar todos los recorridos
    public void cargarRecorridos() {
        recorridosRepository.obtenerRecorridos().observeForever(newData -> {
            recorridosLiveData.setValue(newData); // Actualiza en lugar de reasignar
        });
    }

    // Cargar recorridos de la última semana
    public void cargarRecorridosSemana() {
        recorridosRepository.obtenerRecorridosSemana().observeForever(newData -> {
            recorridosLiveData.setValue(newData); // Actualiza el mismo LiveData
        });
    }

    // Cargar recorridos del último mes
    public void cargarRecorridosMes() {
        recorridosRepository.obtenerRecorridosMes().observeForever(newData -> {
            recorridosLiveData.setValue(newData); // Actualiza el mismo LiveData
        });
    }

    // Obtener LiveData de los recorridos
    public LiveData<AllRecorridosUsuarioResponse> getRecorridosLiveData() {
        return recorridosLiveData;
    }
}
