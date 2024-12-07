package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.RecorridoInicioRepository;
import com.example.bicycles.Responses.RecorridoInicioResponse;

public class RecorridoInicioViewModel extends ViewModel {

    private RecorridoInicioRepository repository;
    private MutableLiveData<RecorridoInicioResponse> recorridoResponse;

    public RecorridoInicioViewModel(Context context) {
        repository = new RecorridoInicioRepository(context);
    }

    public LiveData<RecorridoInicioResponse> iniciarRecorrido(int bicicletaId) {
        if (recorridoResponse == null) {
            recorridoResponse = repository.iniciarRecorrido(bicicletaId);
        }
        return recorridoResponse;
    }
}
