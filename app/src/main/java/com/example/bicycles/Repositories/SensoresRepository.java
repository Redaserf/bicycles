package com.example.bicycles.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.SensoresResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensoresRepository {
    private ApiService apiService;

    public SensoresRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<SensoresResponse> fetchSensores(int recorridoId, String tiempo) {
        MutableLiveData<SensoresResponse> sensoresLiveData = new MutableLiveData<>();

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JsonObject body = new JsonObject();
        body.addProperty("recorrido_id", recorridoId);
        body.addProperty("tiempo", tiempo); // Agrega el tiempo en formato "HH:mm:ss"

        apiService.getSensores(body).enqueue(new Callback<SensoresResponse>() {
            @Override
            public void onResponse(Call<SensoresResponse> call, Response<SensoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sensoresLiveData.setValue(response.body());
                } else {
                    Log.e("SensoresRepository", "Error en la respuesta: " + response.message());
                    sensoresLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SensoresResponse> call, Throwable t) {
                Log.e("SensoresRepository", "Fallo en la conexión: " + t.getMessage());
                sensoresLiveData.setValue(null);
            }
        });

        return sensoresLiveData;
    }

}
