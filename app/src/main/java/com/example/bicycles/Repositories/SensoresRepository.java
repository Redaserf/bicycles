package com.example.bicycles.Repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.SensoresResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensoresRepository {

    private ApiService apiService;

    public SensoresRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<SensoresResponse> getSensores(Context context) {
        MutableLiveData<SensoresResponse> sensoresLiveData = new MutableLiveData<>();

        apiService.getSensores().enqueue(new Callback<SensoresResponse>() {
            @Override
            public void onResponse(Call<SensoresResponse> call, Response<SensoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sensoresLiveData.setValue(response.body());
                } else {
                    Toast.makeText(context, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    sensoresLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SensoresResponse> call, Throwable t) {
                Toast.makeText(context, "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                sensoresLiveData.setValue(null);
            }
        });

        return sensoresLiveData;
    }

}
