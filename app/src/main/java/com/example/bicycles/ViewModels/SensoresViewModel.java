package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.SensoresRepository;
import com.example.bicycles.Responses.SensoresResponse;

public class SensoresViewModel extends ViewModel {

    private MutableLiveData<SensoresResponse> sensoresData;
    private SensoresRepository sensoresRepository;

    public SensoresViewModel(Context context) {
        this.sensoresRepository = new SensoresRepository(context);
    }

    public void fetchSensores(Context context) {
        sensoresData = sensoresRepository.getSensores(context);
    }


    public LiveData<SensoresResponse> getSensoresData() {
        return sensoresData;
    }
}
