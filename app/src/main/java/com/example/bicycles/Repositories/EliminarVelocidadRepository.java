package com.example.bicycles.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.EliminarVelocidadResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EliminarVelocidadRepository {
    private ApiService apiService;

    public EliminarVelocidadRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<EliminarVelocidadResponse> eliminarVelocidades(int recorridoId) {
        MutableLiveData<EliminarVelocidadResponse> responseLiveData = new MutableLiveData<>();

        // Crear el cuerpo de la solicitud
        JsonObject body = new JsonObject();
        body.addProperty("recorrido_id", recorridoId);

        apiService.eliminarVelocidades(body).enqueue(new Callback<EliminarVelocidadResponse>() {
            @Override
            public void onResponse(Call<EliminarVelocidadResponse> call, Response<EliminarVelocidadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.setValue(response.body());
                } else {
                    Log.e("EliminarVelocidadRepo", "Error en la respuesta: " + response.message());
                    responseLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EliminarVelocidadResponse> call, Throwable t) {
                Log.e("EliminarVelocidadRepo", "Fallo en la conexión: " + t.getMessage());
                responseLiveData.setValue(null);
            }
        });


        return responseLiveData;
    }
}
