package com.example.bicycles.ViewModels;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.RegisterResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> registerMessage = new MutableLiveData<>();
    private ApiService apiService;

    public RegisterViewModel(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public void register(String name, String lastName, Double peso, String email, String password, Context context) {
        Usuario usuario = new Usuario(name, lastName, peso, email, password);

        apiService.register(usuario).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Guardar el token en SharedPreferences
                    String token = response.body().getToken();
                    SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.apply();

                    // Actualizar mensaje de éxito
                    registerMessage.setValue("Registro exitoso");

                } else {
                    registerMessage.setValue("Error en el registro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                registerMessage.setValue("Error de red: " + t.getMessage());
            }
        });
    }

    public LiveData<String> getRegisterMessage() {
        return registerMessage;
    }
}
