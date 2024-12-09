package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private ApiService apiService;
    private Context context; // Variable de instancia para el contexto

    public LoginRepository(Context context) {
        this.context = context; // Asigna el contexto al inicializar el repositorio
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResponse = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Guardar el token en SharedPreferences
                    SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();

                    RetrofitClient.resetInstance();

                    loginResponse.setValue("REDIRECT");
                } else if (response.code() == 403) {
                    // Usuario no verificado, redirigir a verificación
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        if (jsonObject.has("redirect") && "verify_code".equals(jsonObject.getString("redirect"))) {
                            loginResponse.setValue("VERIFY_CODE");
                        } else {
                            loginResponse.setValue(jsonObject.getString("mensaje"));
                        }
                    } catch (Exception e) {
                        loginResponse.setValue("Error inesperado.");
                        e.printStackTrace();
                    }
                } else {
                    // Manejo de errores normales
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        loginResponse.setValue(jsonObject.getString("mensaje"));
                    } catch (Exception e) {
                        loginResponse.setValue("Error inesperado.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponse.setValue("Error de red: " + t.getMessage());
            }
        });

        return loginResponse;
    }
}
