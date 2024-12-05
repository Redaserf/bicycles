package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.UsuarioRepository;
import com.example.bicycles.Responses.UsuarioResponse;

public class UsuarioViewModel extends ViewModel {
    private UsuarioRepository usuarioRepository;
    private LiveData<UsuarioResponse.Usuario> usuarioLiveData;

    // Constructor
    public UsuarioViewModel(Context context) {
        usuarioRepository = new UsuarioRepository(context);
        usuarioLiveData = usuarioRepository.obtenerUsuario();
    }

    // Método para cargar usuario (opcional si quieres recargar manualmente)
    public void cargarUsuario() {
        usuarioLiveData = usuarioRepository.obtenerUsuario();
    }

    // Obtener LiveData del usuario
    public LiveData<UsuarioResponse.Usuario> getUsuarioLiveData() {
        return usuarioLiveData;
    }
}
