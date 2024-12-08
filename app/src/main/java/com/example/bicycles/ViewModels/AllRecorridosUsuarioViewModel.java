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
    }

    // Cargar todos los recorridos
    public void cargarRecorridos() {
        recorridosLiveData = recorridosRepository.obtenerRecorridos();
    }

    // Cargar recorridos de la última semana
    public void cargarRecorridosSemana() {
        recorridosLiveData = recorridosRepository.obtenerRecorridosSemana();
    }

    // Cargar recorridos del último mes
    public void cargarRecorridosMes() {
        recorridosLiveData = recorridosRepository.obtenerRecorridosMes();
    }

    // Obtener LiveData de los recorridos
    public LiveData<AllRecorridosUsuarioResponse> getRecorridosLiveData() {
        return recorridosLiveData;
    }
}
