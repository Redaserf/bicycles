package com.example.bicycles.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicicletasMasRepository {

    private ApiService apiService;

    public BicicletasMasRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<List<Bicicleta>> fetchBicicletas() {
        MutableLiveData<List<Bicicleta>> liveData = new MutableLiveData<>();

        apiService.getBicicletas().enqueue(new Callback<MisBicicletasResponse>() {
            @Override
            public void onResponse(Call<MisBicicletasResponse> call, Response<MisBicicletasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DEBUG_API", "Bicicletas recibidas: " + response.body().getBicicletas());
                    liveData.postValue(response.body().getBicicletas());
                } else {
                    Log.d("DEBUG_API", "Error en la respuesta: " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MisBicicletasResponse> call, Throwable t) {
                Log.d("DEBUG_API", "Fallo en la solicitud: " + t.getMessage());
                liveData.postValue(null);
            }

        });

        return liveData;
    }
}
