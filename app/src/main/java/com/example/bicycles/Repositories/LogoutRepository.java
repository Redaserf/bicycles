package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.LogoutResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutRepository {
    private final ApiService apiService;
    private final Context context;

    public LogoutRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
        this.context = context;
    }

    public MutableLiveData<String> logout() {
        MutableLiveData<String> logoutResponse = new MutableLiveData<>();

        apiService.logout().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Limpiar el SharedPreferences
                    SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("token");
                    editor.apply();

                    logoutResponse.setValue(response.body().getMensaje());
                } else {
                    logoutResponse.setValue("Error al cerrar sesión");
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                logoutResponse.setValue("Error de red: " + t.getMessage());
            }
        });

        return logoutResponse;
    }
}