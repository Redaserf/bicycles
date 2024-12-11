package com.example.bicycles.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Models.EncenderRequest;
import com.example.bicycles.Responses.LuzResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LuzRepository {
    private final ApiService apiService;

    public LuzRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<Integer> cambiarEstadoLuz(Integer encender) {
        MutableLiveData<Integer> luzLiveData = new MutableLiveData<>();
        EncenderRequest request = new EncenderRequest(encender);

        apiService.getEstadoLuz(request).enqueue(new Callback<LuzResponse>() {
            @Override
            public void onResponse(Call<LuzResponse> call, Response<LuzResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    luzLiveData.setValue(response.body().getLuz());
                    Log.d("DEBUG", "Respuesta exitosa: " + response.body().getLuz());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
                        Log.e("DEBUG", "Error en la respuesta: " + errorBody);
                    } catch (IOException e) {
                        Log.e("DEBUG", "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                    luzLiveData.setValue(null); // En caso de error, devolver null
                }
            }

            @Override
            public void onFailure(Call<LuzResponse> call, Throwable throwable) {
                Log.e("DEBUG", "Error en la API: " + throwable.getMessage());
                luzLiveData.setValue(null); // En caso de fallo, devolver null
            }
        });
        return luzLiveData;
    }
}
