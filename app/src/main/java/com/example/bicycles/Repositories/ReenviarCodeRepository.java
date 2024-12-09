package com.example.bicycles.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.ReenviarRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.ReenviarResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReenviarCodeRepository {
    private ApiService apiService;

    public ReenviarCodeRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> reenviarCodigo(String email) {
        MutableLiveData<String> reenviarResponse = new MutableLiveData<>();

        ReenviarRequest request = new ReenviarRequest(email);

        apiService.reenviar(request).enqueue(new Callback<ReenviarResponse>() {
            @Override
            public void onResponse(Call<ReenviarResponse> call, Response<ReenviarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reenviarResponse.setValue(response.body().getMensaje());
                } else {
                    reenviarResponse.setValue("Error al reenviar el código.");
                }
            }

            @Override
            public void onFailure(Call<ReenviarResponse> call, Throwable t) {
                reenviarResponse.setValue("Error de red: " + t.getMessage());
            }
        });

        return reenviarResponse;
    }
}
