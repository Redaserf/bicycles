package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
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
    private Context context;

    public MisBicicletasRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<List<Bicicleta>> getBicicletas() {
        MutableLiveData<List<Bicicleta>> bicicletasLiveData = new MutableLiveData<>();

        apiService.getBicicletas().enqueue(new Callback<MisBicicletasResponse>() {
            @Override
            public void onResponse(Call<MisBicicletasResponse> call, Response<MisBicicletasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bicicletasLiveData.postValue(response.body().getBicicletas());
                } else {
                    bicicletasLiveData.postValue(new ArrayList<>()); // Vacía si hay error
                }
            }

            @Override
            public void onFailure(Call<MisBicicletasResponse> call, Throwable throwable) {
                bicicletasLiveData.postValue(new ArrayList<>()); // Vacía si falla la solicitud
            }
        });

        return bicicletasLiveData;
    }

}