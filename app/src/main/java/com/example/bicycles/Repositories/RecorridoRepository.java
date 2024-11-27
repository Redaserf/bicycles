package com.example.bicycles.Repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Recorrido;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.RecorridoResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import java.time.LocalTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecorridoRepository {

    private ApiService apiService;

    public RecorridoRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public MutableLiveData<String> addRecorrido(int usuario_id, int bicicleta_id, String tiempo, double calorias,
                                                double velocidadPromedio, double velocidadMaxima, double distanciaRecorrida, Context context) {
        MutableLiveData<String> recorridoResponse = new MutableLiveData<>();
        Recorrido recorrido = new Recorrido(usuario_id, bicicleta_id, LocalTime.parse(tiempo));
        recorrido.setCalorias(calorias);
        recorrido.setVelocidad_promedio(velocidadPromedio);
        recorrido.setVelocidad_maxima(velocidadMaxima);
        recorrido.setDistancia_recorrida(distanciaRecorrida);

        apiService.addRecorrido(recorrido).enqueue(new Callback<RecorridoResponse>() {
            @Override
            public void onResponse(Call<RecorridoResponse> call, Response<RecorridoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al agregar recorrido", Toast.LENGTH_SHORT).show();
                }
                recorridoResponse.setValue(response.body() != null ? response.body().getMensaje() : "Error desconocido");
            }

            @Override
            public void onFailure(Call<RecorridoResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                recorridoResponse.setValue(t.getMessage());
            }
        });

        return recorridoResponse;
    }

    public MutableLiveData<Recorrido> getRecorridoById(int recorridoId, Context context) {
        MutableLiveData<Recorrido> recorridoData = new MutableLiveData<>();

        apiService.getRecorridoById(recorridoId).enqueue(new Callback<RecorridoResponse>() {
            @Override
            public void onResponse(Call<RecorridoResponse> call, Response<RecorridoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recorridoData.setValue(response.body().getRecorrido());
                } else {
                    Toast.makeText(context, "Error al obtener el recorrido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecorridoResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return recorridoData;
    }
}
