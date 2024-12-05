package com.example.bicycles.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.UsuarioResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private ApiService apiService;

    public UsuarioRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<UsuarioResponse.Usuario> obtenerUsuario() {
        MutableLiveData<UsuarioResponse.Usuario> usuarioLiveData = new MutableLiveData<>();

        apiService.getUsuario().enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuarioLiveData.setValue(response.body().getUsuario());
                } else {
                    usuarioLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                usuarioLiveData.setValue(null);
            }
        });

        return usuarioLiveData;
    }
}
