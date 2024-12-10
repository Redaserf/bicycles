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
    private Context context;

    public LoginRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResponse = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Guardar el token
                    SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();

                    RetrofitClient.resetInstance();
                    loginResponse.setValue("REDIRECT");
                } else if (response.code() == 403) {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);

                        if (jsonObject.has("redirect") && "verify_code".equals(jsonObject.getString("redirect"))) {
                            loginResponse.setValue("VERIFY_CODE");
                        } else {
                            loginResponse.setValue(jsonObject.optString("mensaje", "Error inesperado"));
                        }
                    } catch (Exception e) {
                        loginResponse.setValue("Error inesperado");
                        e.printStackTrace();
                    }
                } else if (response.code() == 422) {
                    // Manejar errores de validación
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);

                        if (jsonObject.has("errores")) {
                            JSONObject errores = jsonObject.getJSONObject("errores");
                            StringBuilder errorMessage = new StringBuilder();

                            if (errores.has("email")) {
                                errorMessage.append(errores.getJSONArray("email").getString(0)).append("\n");
                            }
                            if (errores.has("password")) {
                                errorMessage.append(errores.getJSONArray("password").getString(0));
                            }

                            loginResponse.setValue(errorMessage.toString().trim());
                        } else {
                            loginResponse.setValue("Error desconocido");
                        }
                    } catch (Exception e) {
                        loginResponse.setValue("Error inesperado");
                        e.printStackTrace();
                    }
                } else {
                    loginResponse.setValue("Credenciales inválidas");
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
