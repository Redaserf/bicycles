package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.Views.Fragments.MisBicisFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicicletaRepository {

    private ApiService apiService;
    private Context context;

    public BicicletaRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<BicicletaResponse> addBicicleta(String nombre) {
        MutableLiveData<BicicletaResponse> bicicletaResponse = new MutableLiveData<>();
        Bicicleta bicicleta = new Bicicleta(nombre);
        apiService.addBicicleta(bicicleta).enqueue(new Callback<BicicletaResponse>() {
            @Override
            public void onResponse(Call<BicicletaResponse> call, Response<BicicletaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bicicletaResponse.setValue(response.body());
                    Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MisBicisFragment.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error al agregar bicicleta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BicicletaResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                BicicletaResponse response = new BicicletaResponse();
                response.setMensaje(t.getMessage());
                bicicletaResponse.setValue(response);
            }
        });
        return bicicletaResponse;
    }

}
