package com.example.bicycles.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.UsuarioEditResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEditRepository {
    private ApiService apiService;

    public UsuarioEditRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> actualizarPerfil(String nombre, String apellido, Double peso, String correo, Context context) {
        MutableLiveData<String> responseMessage = new MutableLiveData<>();

        // Crear el objeto Usuario con los datos proporcionados
        Usuario usuario = new Usuario(nombre, apellido, peso, correo);

        System.out.println("Datos enviados: " + usuario.getNombre() + ", " + usuario.getApellido() + ", " + usuario.getPeso() + ", " + usuario.getEmail());

        apiService.actualizarUsuario(usuario).enqueue(new Callback<UsuarioEditResponse>() {
            @Override
            public void onResponse(Call<UsuarioEditResponse> call, Response<UsuarioEditResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseMessage.setValue("Perfil actualizado correctamente");
                } else {
                    responseMessage.setValue("Error al actualizar el perfil");
                }
            }

            @Override
            public void onFailure(Call<UsuarioEditResponse> call, Throwable t) {
                responseMessage.setValue("Error de red");
            }
        });

        return responseMessage;
    }

}
