package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.BicicletaRepository;

public class BicicletaViewModel extends ViewModel {

    private MutableLiveData<String> bicicletaMessage;
    private BicicletaRepository bicicletaRepository;

    public BicicletaViewModel(Context context) {
        this.bicicletaRepository = new BicicletaRepository(context);
    }

    public void addBicicleta(String nombre, int usuario_id, Context context) {
        bicicletaMessage = bicicletaRepository.addBicicleta(nombre, usuario_id, context);
    }

    public LiveData<String> getBicicletaMessage() {
        return bicicletaMessage;
    }
}
