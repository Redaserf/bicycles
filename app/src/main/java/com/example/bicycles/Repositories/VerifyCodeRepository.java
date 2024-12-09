package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.VerifyCodeRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.VerifyCodeResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeRepository {

    private ApiService apiService;
    private Context context;

    public VerifyCodeRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> verifyCode(String email, String code) {
        MutableLiveData<String> verifyResponse = new MutableLiveData<>();
        VerifyCodeRequest request = new VerifyCodeRequest(email, code);

        apiService.verifyCode(request).enqueue(new Callback<VerifyCodeResponse>() {
            @Override
            public void onResponse(Call<VerifyCodeResponse> call, Response<VerifyCodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    if (token != null) {
                        // Guardar el token en SharedPreferences
                        SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", token);
                        editor.apply();

                        verifyResponse.setValue("VERIFICADO");
                    } else {
                        verifyResponse.setValue(response.body().getMensaje());
                    }
                } else {
                    verifyResponse.setValue("Error al verificar el código.");
                }
            }

            @Override
            public void onFailure(Call<VerifyCodeResponse> call, Throwable t) {
                verifyResponse.setValue("Error de red: " + t.getMessage());
            }
        });

        return verifyResponse;
    }
}
