package com.example.bicycles.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.BicicletaRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Repositories.BicicletaRepository;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.EditarBicicletaResponse;
import com.example.bicycles.Responses.EliminarBicicletaResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicicletaViewModel extends ViewModel {

    private MutableLiveData<BicicletaResponse> bicicleta = new MutableLiveData<>();
    private MutableLiveData<EliminarBicicletaResponse> eliminarResponse = new MutableLiveData<>();
    private MutableLiveData<EditarBicicletaResponse> editarResponse = new MutableLiveData<>();

//    private BicicletaRepository bicicletaRepository;

    private ApiService apiService;

    private Context context;

    public BicicletaViewModel(Context context) {
        this.context = context;
//        this.bicicletaRepository = new BicicletaRepository(context);
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public void addBicicleta(MultipartBody.Part imagen, RequestBody nombre) {
        apiService.addBicicleta(imagen, nombre).enqueue(new Callback<BicicletaResponse>() {
            @Override
            public void onResponse(Call<BicicletaResponse> call, Response<BicicletaResponse> response) {
                if(response.isSuccessful()){

                    bicicleta.postValue(response.body());

//                    Log.d("DEBUG", "Se trajo correctamente la bicicleta codigo: " + response.code());

                }else{
                    bicicleta.postValue(null);
//                    Log.d("DEBUG", "No respondio satistactoriamente");

                }
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
                bicicleta.postValue(null);
            }
        });
//        bicicleta = bicicletaRepository.addBicicleta(imagen, nombre);
    }

    public LiveData<BicicletaResponse> getBicicletaCreated() {
        return bicicleta;
    }

    public void eliminarBicicleta(int id){
        apiService.eliminarBicicleta(id).enqueue(new Callback<EliminarBicicletaResponse>() {
            @Override
            public void onResponse(Call<EliminarBicicletaResponse> call, Response<EliminarBicicletaResponse> response) {
                if(response.isSuccessful() && response.body() != null){
//                    Log.d("BicicletaRepository", response.body().getMensaje());

                    eliminarResponse.setValue(response.body());
                }else{
                    Log.d("BicicletaRepository", String.valueOf(response.code()));
                    eliminarResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EliminarBicicletaResponse> call, Throwable throwable) {
                eliminarResponse.setValue(null);
            }
        });
//        eliminarResponse = bicicletaRepository.eliminarBicicleta(id, context);
    }

    public LiveData<EliminarBicicletaResponse> getElimnarBicicleta(){
        return eliminarResponse;
    }

    public LiveData<EditarBicicletaResponse> getEditarResponse(){
        return editarResponse;
    }
    public void editarBicicleta(int id, MultipartBody.Part imagen, RequestBody nombre){
        Log.d("DEBUG", "se intento editar la bici " +  nombre.toString());
        apiService.editarBicicleta(id, imagen, nombre).enqueue(new Callback<EditarBicicletaResponse>() {
            @Override
            public void onResponse(Call<EditarBicicletaResponse> call, Response<EditarBicicletaResponse> response) {
                if (response.isSuccessful()) {
                    editarResponse.postValue(response.body());
                } else {
                    editarResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<EditarBicicletaResponse> call, Throwable throwable) {
                editarResponse.postValue(null);
            }
        });
    }
}
