package com.example.bicycles.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Models.VerifyCodeRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.RegisterResponse;
import com.example.bicycles.Responses.VerifyCodeResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {

    private ApiService apiService;

    public RegisterRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    // Método para registrar usuario
    public MutableLiveData<String> register(String name, String lastName, Double peso, String email, String password) {
        MutableLiveData<String> registerResponse = new MutableLiveData<>();
        Usuario usuario = new Usuario(name, lastName, peso, email, password);

        apiService.register(usuario).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registerResponse.setValue("Registro exitoso");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);

                        if (jsonObject.has("errores")) {
                            JSONObject errores = jsonObject.getJSONObject("errores");
                            StringBuilder errorMessage = new StringBuilder();

                            if (errores.has("nombre")) {
                                errorMessage.append(errores.getJSONArray("nombre").getString(0)).append("\n");
                            }
                            if (errores.has("apellido")) {
                                errorMessage.append(errores.getJSONArray("apellido").getString(0)).append("\n");
                            }
                            if (errores.has("peso")) {
                                errorMessage.append(errores.getJSONArray("peso").getString(0)).append("\n");
                            }
                            if (errores.has("email")) {
                                errorMessage.append(errores.getJSONArray("email").getString(0)).append("\n");
                            }
                            if (errores.has("password")) {
                                errorMessage.append(errores.getJSONArray("password").getString(0));
                            }

                            registerResponse.setValue(errorMessage.toString().trim());
                        } else {
                            registerResponse.setValue("Error desconocido");
                        }
                    } catch (Exception e) {
                        registerResponse.setValue("Error inesperado");
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                registerResponse.setValue("Error de red: " + t.getMessage());
            }
        });

        return registerResponse;
    }

    // Método para verificar el código
    public MutableLiveData<String> verifyCode(String email, String codigo) {
        MutableLiveData<String> verifyResponse = new MutableLiveData<>();
        VerifyCodeRequest request = new VerifyCodeRequest(email, codigo);

        apiService.verifyCode(request).enqueue(new Callback<VerifyCodeResponse>() {
            @Override
            public void onResponse(Call<VerifyCodeResponse> call, Response<VerifyCodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    verifyResponse.setValue(response.body().getMensaje());
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