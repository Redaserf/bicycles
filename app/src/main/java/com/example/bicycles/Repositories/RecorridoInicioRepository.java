package com.example.bicycles.Repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.RecorridoInicioResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecorridoInicioRepository {

    private ApiService apiService;
    private Context context;

    public RecorridoInicioRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<RecorridoInicioResponse> iniciarRecorrido(int bicicletaId) {
        MutableLiveData<RecorridoInicioResponse> responseLiveData = new MutableLiveData<>();

        Log.d("RecorridoInicio", "Iniciando recorrido con bicicleta ID: " + bicicletaId);

        apiService.iniciarRecorrido(bicicletaId).enqueue(new Callback<RecorridoInicioResponse>() {
            @Override
            public void onResponse(Call<RecorridoInicioResponse> call, Response<RecorridoInicioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.setValue(response.body());
                } else {
                    try {
                        // Leer el contenido del error
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(context, "Error al iniciar recorrido: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("RecorridoInicio", "Error en la respuesta: " + errorMessage);
                    } catch (Exception e) {
                        Log.e("RecorridoInicio", "Error al leer el cuerpo de la respuesta: " + e.getMessage());
                    }
                }
            }



            @Override
            public void onFailure(Call<RecorridoInicioResponse> call, Throwable t) {
                Log.e("RecorridoInicio", "Error de conexión: " + t.getMessage());
                responseLiveData.setValue(null);
                Toast.makeText(context, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return responseLiveData;
    }

}
