package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.BicicletaRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.EditarBicicletaResponse;
import com.example.bicycles.Responses.EliminarBicicletaResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.ViewModels.BicicletaViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.Views.Fragments.MisBicisFragment;
import com.example.bicycles.Views.Home;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicicletaRepository {

    private ApiService apiService;
    private Context context;

    public BicicletaRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<Bicicleta> addBicicleta(BicicletaRequest nombre) {
        MutableLiveData<Bicicleta> bicicletaResponse = new MutableLiveData<>();

        apiService.addBicicleta(nombre).enqueue(new Callback<BicicletaResponse>() {
            @Override
            public void onResponse(Call<BicicletaResponse> call, Response<BicicletaResponse> response) {
                if(response.isSuccessful()){

                    bicicletaResponse.postValue(response.body().getBicicleta());
                    Log.d("DEBUG", "Se trajo correctamente la bicicleta");

                }
                bicicletaResponse.postValue(null);
                int code = response.code();
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        Log.e("DEBUG", "ErrorBody: " + errorJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<BicicletaResponse> call, Throwable throwable) {

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

    public MutableLiveData<EditarBicicletaResponse> editarBicicleta(int id, BicicletaRequest nombre){
        MutableLiveData<EditarBicicletaResponse> editarResponse = new MutableLiveData<>();
        Log.d("DEBUG", "Nombre de la bici que remplaza el anterior: " + nombre);

        apiService.editarBicicleta(id, nombre).enqueue(new Callback<EditarBicicletaResponse>() {
            @Override
            public void onResponse(Call<EditarBicicletaResponse> call, Response<EditarBicicletaResponse> response) {
                if(response.isSuccessful()){

                    editarResponse.postValue(response.body());
                    Log.d("DEBUG", "Se trajo correctamente la bicicleta" + response.body().getBicicleta().getNombre());

                }
                editarResponse.postValue(null);
                int code = response.code();
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        Log.e("DEBUG", "ErrorBody: " + errorJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditarBicicletaResponse> call, Throwable throwable) {
                editarResponse.setValue(null);

            }
        });

        return editarResponse;
    }

}
