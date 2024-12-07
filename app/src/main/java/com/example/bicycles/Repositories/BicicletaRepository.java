package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.EditarBicicletaResponse;
import com.example.bicycles.Responses.EliminarBicicletaResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.Views.Fragments.MisBicisFragment;
import com.example.bicycles.Views.Home;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicicletaRepository {

    private ApiService apiService;
    private Context context;

    public BicicletaRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<BicicletaResponse> addBicicleta(String nombre) {
        MutableLiveData<BicicletaResponse> bicicletaResponse = new MutableLiveData<>();
        Bicicleta bicicleta = new Bicicleta(nombre);
        apiService.addBicicleta(bicicleta).enqueue(new Callback<BicicletaResponse>() {
            @Override
            public void onResponse(Call<BicicletaResponse> call, Response<BicicletaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bicicletaResponse.setValue(response.body());
                    Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent( context, Home.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error al agregar bicicleta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BicicletaResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                BicicletaResponse response = new BicicletaResponse();
                response.setMensaje(t.getMessage());
                bicicletaResponse.setValue(response);
            }
        });
        return bicicletaResponse;
    }

    public MutableLiveData<EliminarBicicletaResponse> eliminarBicicleta(int id){
        MutableLiveData<EliminarBicicletaResponse> liveData = new MutableLiveData<>();

        apiService.eliminarBicicleta(id).enqueue(new Callback<EliminarBicicletaResponse>() {
            @Override
            public void onResponse(Call<EliminarBicicletaResponse> call, Response<EliminarBicicletaResponse> response) {
                if(response.isSuccessful()){
                    Log.d("BicicletaRepository", response.body().getMensaje());
                    liveData.setValue(response.body());
                }else{
                    Log.d("BicicletaRepository", response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<EliminarBicicletaResponse> call, Throwable throwable) {
                Log.d("BicicletaRepository", throwable.getMessage());
            }
        });

        return liveData;
    }

    public MutableLiveData<EditarBicicletaResponse> editarBicicleta(int id){
        MutableLiveData<EditarBicicletaResponse> editarResponse = new MutableLiveData<>();

        apiService.editarBicicleta(id).enqueue(new Callback<EditarBicicletaResponse>() {
            @Override
            public void onResponse(Call<EditarBicicletaResponse> call, Response<EditarBicicletaResponse> response) {
                editarResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<EditarBicicletaResponse> call, Throwable throwable) {
                editarResponse.setValue(null);

            }
        });

        return editarResponse;
    }

}
