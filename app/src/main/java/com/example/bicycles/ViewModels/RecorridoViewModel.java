package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Models.Recorrido;
import com.example.bicycles.Repositories.RecorridoRepository;

public class RecorridoViewModel extends ViewModel {

    private MutableLiveData<String> recorridoMessage;
    private MutableLiveData<Recorrido> recorridoData;
    private RecorridoRepository recorridoRepository;

    public RecorridoViewModel(Context context) {
        this.recorridoRepository = new RecorridoRepository(context);
    }

    public void addRecorrido(int usuario_id, int bicicleta_id, String tiempo, double calorias,
                             double velocidadPromedio, double velocidadMaxima, double distanciaRecorrida, Context context) {
        recorridoMessage = recorridoRepository.addRecorrido(usuario_id, bicicleta_id, tiempo, calorias,
                velocidadPromedio, velocidadMaxima, distanciaRecorrida, context);
    }

    public LiveData<String> getRecorridoMessage() {
        return recorridoMessage;
    }

    public void fetchRecorridoById(int recorridoId, Context context) {
        recorridoData = recorridoRepository.getRecorridoById(recorridoId, context);
    }

    public LiveData<Recorrido> getRecorridoData() {
        return recorridoData;
    }
}
