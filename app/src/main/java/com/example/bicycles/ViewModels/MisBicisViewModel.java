package com.example.bicycles.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Repositories.MisBicicletasRepository;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Singleton.RetrofitClient;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisBicisViewModel extends ViewModel {

    private final MutableLiveData<List<Bicicleta>> bicicletasLiveData = new MutableLiveData<>();

    private Context context;
    private ApiService apiService;


    public MisBicisViewModel(Context context){
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();

    }

    public LiveData<List<Bicicleta>> getBicicletasLiveData() {
        return bicicletasLiveData;
    }
    

    public void fetchBicicletas() {
        apiService.getBicicletas().enqueue(new Callback<MisBicicletasResponse>() {
            @Override
            public void onResponse(Call<MisBicicletasResponse> call, Response<MisBicicletasResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getBicicletas() != null){
                        bicicletasLiveData.setValue(new ArrayList<>(response.body().getBicicletas()));
                    }
                }

            }

            @Override
            public void onFailure(Call<MisBicicletasResponse> call, Throwable throwable) {
                Toast.makeText(context, "NO Respondio la api", Toast.LENGTH_SHORT).show();

            }
        });
    }

}