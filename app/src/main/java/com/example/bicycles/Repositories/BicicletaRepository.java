package com.example.bicycles.Repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicicletaRepository {

    private ApiService apiService;

    public BicicletaRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> addBicicleta(String nombre, int usuario_id, Context context) {
        MutableLiveData<String> bicicletaResponse = new MutableLiveData<>();
        Bicicleta bicicleta = new Bicicleta(nombre, usuario_id);
        apiService.addBicicleta(bicicleta).enqueue(new Callback<BicicletaResponse>() {
            @Override
            public void onResponse(Call<BicicletaResponse> call, Response<BicicletaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al agregar bicicleta", Toast.LENGTH_SHORT).show();
                }
                bicicletaResponse.setValue(response.body() != null ? response.body().getMensaje() : "Error desconocido");
            }

            @Override
            public void onFailure(Call<BicicletaResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                bicicletaResponse.setValue(t.getMessage());
            }
        });
        return bicicletaResponse;
    }

}
