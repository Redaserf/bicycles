package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.RegisterResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.Views.Fragments.MasFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {

    private ApiService apiService;

    public RegisterRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> register(String name, String lastName, String email, String password, Context context) {
        MutableLiveData<String> registerResponse = new MutableLiveData<>();
        Usuario usuario = new Usuario(name, lastName, email, password);
        apiService.register(usuario).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();

                    // Redirigir a la actividad deseada (activity_mas)
                    Intent intent = new Intent(context, MasFragment.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Necesario al iniciar actividad desde un contexto no UI
                    context.startActivity(intent);
                } else {
                    // Manejar error en el registro
                    Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
                registerResponse.setValue(response.body() != null ? response.body().getMensaje() : "Error desconocido");
            }


            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();
                registerResponse.setValue(t.getMessage());
            }
        });
        return registerResponse;
    }

}
