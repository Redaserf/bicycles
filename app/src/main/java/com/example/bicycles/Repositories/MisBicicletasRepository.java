package com.example.bicycles.Repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisBicicletasRepository {

    private ApiService apiService;
    private Context context;

    public MisBicicletasRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<List<Bicicleta>> getMisBicicletas() {
        MutableLiveData<List<Bicicleta>> bicicletas = new MutableLiveData<>();

        apiService.getBicicletas().enqueue(new Callback<MisBicicletasResponse>() {
            @Override
            public void onResponse(Call<MisBicicletasResponse> call, Response<MisBicicletasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bicicletas.setValue(response.body().getBicicletas());
                } else {
                    bicicletas.setValue(null);
                    Toast.makeText(context, "Error al obtener las bicicletas: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MisBicicletasResponse> call, Throwable throwable) {
                bicicletas.setValue(null);
                Toast.makeText(context, "Error de red: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return bicicletas;
    }
}
