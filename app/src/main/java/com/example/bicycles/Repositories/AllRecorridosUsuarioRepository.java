package com.example.bicycles.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRecorridosUsuarioRepository {
    private ApiService apiService;

    public AllRecorridosUsuarioRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<AllRecorridosUsuarioResponse> obtenerRecorridos() {
        MutableLiveData<AllRecorridosUsuarioResponse> recorridosLiveData = new MutableLiveData<>();

        apiService.getRecorridos().enqueue(new Callback<AllRecorridosUsuarioResponse>() {
            @Override
            public void onResponse(Call<AllRecorridosUsuarioResponse> call, Response<AllRecorridosUsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recorridosLiveData.setValue(response.body());
                } else {
                    recorridosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AllRecorridosUsuarioResponse> call, Throwable t) {
                recorridosLiveData.setValue(null);
            }
        });

        return recorridosLiveData;
    }
}
