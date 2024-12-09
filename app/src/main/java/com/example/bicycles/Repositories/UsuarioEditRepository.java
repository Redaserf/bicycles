package com.example.bicycles.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.UsuarioEditResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEditRepository {
    private ApiService apiService;

    public UsuarioEditRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> actualizarPerfil(String nombre, String apellido, Double peso, String correo, Context context) {
        MutableLiveData<String> responseMessage = new MutableLiveData<>();

        // Crear el objeto Usuario con los datos proporcionados
        Usuario usuario = new Usuario(nombre, apellido, peso, correo);

        apiService.actualizarUsuario(usuario).enqueue(new Callback<UsuarioEditResponse>() {
            @Override
            public void onResponse(Call<UsuarioEditResponse> call, Response<UsuarioEditResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseMessage.setValue("Perfil actualizado correctamente");
                } else {
                    try {
                        // Procesar los errores de validación
                        String errorBody = response.errorBody().string();
                        System.out.println("Error Body: " + errorBody); // Log para depuración
                        JSONObject jsonObject = new JSONObject(errorBody);

                        if (jsonObject.has("errors")) {
                            JSONObject errors = jsonObject.getJSONObject("errors");
                            StringBuilder errorMessages = new StringBuilder();

                            if (errors.has("nombre")) {
                                errorMessages.append(errors.getJSONArray("nombre").getString(0)).append("\n");
                            }
                            if (errors.has("apellido")) {
                                errorMessages.append(errors.getJSONArray("apellido").getString(0)).append("\n");
                            }
                            if (errors.has("peso")) {
                                errorMessages.append(errors.getJSONArray("peso").getString(0)).append("\n");
                            }
                            if (errors.has("email")) {
                                errorMessages.append(errors.getJSONArray("email").getString(0));
                            }

                            responseMessage.setValue(errorMessages.toString().trim());
                        } else if (jsonObject.has("message")) {
                            responseMessage.setValue(jsonObject.getString("message"));
                        } else {
                            responseMessage.setValue("Error desconocido.");
                        }
                    } catch (Exception e) {
                        responseMessage.setValue("Error inesperado al procesar la respuesta.");
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<UsuarioEditResponse> call, Throwable t) {
                responseMessage.setValue("Error de red: " + t.getMessage());
            }
        });

        return responseMessage;
    }
}
