package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.BicicletaRepository;
import com.example.bicycles.Responses.BicicletaResponse;

public class BicicletaViewModel extends ViewModel {

    private MutableLiveData<BicicletaResponse> bicicletaMessage;
    private BicicletaRepository bicicletaRepository;

    public BicicletaViewModel(Context context) {
        this.bicicletaRepository = new BicicletaRepository(context);
    }

    public void addBicicleta(String nombre) {
        bicicletaMessage = bicicletaRepository.addBicicleta(nombre);
    }

    public LiveData<BicicletaResponse> getBicicletaResponse() {
        return bicicletaMessage;
    }
}
