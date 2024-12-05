package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.BicicletaRepository;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.EditarBicicletaResponse;
import com.example.bicycles.Responses.EliminarBicicletaResponse;

public class BicicletaViewModel extends ViewModel {

    private MutableLiveData<BicicletaResponse> bicicletaMessage;
    private MutableLiveData<EliminarBicicletaResponse> eliminarResponse;
    private MutableLiveData<EditarBicicletaResponse> editarResponse;

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

    public void eliminarBicicleta(int id){
        eliminarResponse = bicicletaRepository.eliminarBicicleta(id);
    }

    public void editarBicicleta(int id){
        editarResponse = bicicletaRepository.editarBicicleta(id);
    }
}
