package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.Views.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisBicicletasRepository {

    private ApiService apiService;

    public MisBicicletasRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<List<Bicicleta>> getMisBicicletas() {
        MutableLiveData<List<Bicicleta>> bicicletas = new MutableLiveData<>();
//        MutableLiveData<String> mensaje = new MutableLiveData<>();

        apiService.getBicicletas().enqueue(new Callback<MisBicicletasResponse>() {
            @Override
            public void onResponse(Call<MisBicicletasResponse> call, Response<MisBicicletasResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Bicicleta> listaBicicletas = response.body().getBicicletas();
                    bicicletas.setValue(listaBicicletas);
//                    mensaje.setValue(response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<MisBicicletasResponse> call, Throwable throwable) {
                bicicletas.setValue(null);
//                mensaje.setValue(throwable.getMessage());
            }
        });

        return bicicletas;
    }

}
