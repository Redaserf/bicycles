package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.UsuarioEditRepository;

public class UsuarioEditViewModel extends ViewModel {

    private MutableLiveData<String> responseMessage;
    private UsuarioEditRepository usuarioEditRepository;

    public UsuarioEditViewModel(Context context) {
        this.usuarioEditRepository = new UsuarioEditRepository(context);
    }

    public void actualizarPerfil(String nombre, String apellido, Double peso, String correo, Context context) {
        responseMessage = usuarioEditRepository.actualizarPerfil(nombre, apellido, peso, correo, context);
    }

    public LiveData<String> getResponseMessage() {
        return responseMessage;
    }
}
