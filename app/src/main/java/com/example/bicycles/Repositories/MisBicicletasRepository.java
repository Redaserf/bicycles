package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.Views.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisBicicletasRepository {

    private ApiService apiService;
    private Context context;

    public MisBicicletasRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<List<Bicicleta>> getMisBicicletas() {
        MutableLiveData<List<Bicicleta>> bicicletas = new MutableLiveData<>();
//        MutableLiveData<String> mensaje = new MutableLiveData<>();
        Toast.makeText(context, "Trayendo las bicics", Toast.LENGTH_LONG).show();
        Log.d("MisBicicletasRepository", "Antes de llamar a enqueue()");
        apiService.getBicicletas().enqueue(new Callback<MisBicicletasResponse>() {
            @Override
            public void onResponse(Call<MisBicicletasResponse> call, Response<MisBicicletasResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getBicicletas() != null){
                        List<Bicicleta> listabici = response.body().getBicicletas();

                        if(listabici.size() <= 0){
                            bicicletas.setValue(null);
                        }else{
                            Log.d("MIsBIcisi", "Existen biciccletas" + listabici.size());
                            bicicletas.setValue(listabici);
                        }
                    }else{
                        bicicletas.setValue(null);
                    }
                }else{
                    Toast.makeText(context, "Algo salio mal en la respuesta", Toast.LENGTH_SHORT).show();
                }

                Log.d("MisBicicletasRepository", "onResponse ejecutado");
            }

            @Override
            public void onFailure(Call<MisBicicletasResponse> call, Throwable throwable) {
                bicicletas.setValue(null);
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MisBicicletasRepository", "onFailure ejecutado: " + throwable.getMessage());
            }
        });
        Log.d("MisBicicletasRepository", "Después de llamar a enqueue()");


        return bicicletas;
    }

}