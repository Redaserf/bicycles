package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.SensoresRepository;
import com.example.bicycles.Responses.SensoresResponse;

public class SensoresViewModel extends ViewModel {
    private SensoresRepository sensoresRepository;

    public SensoresViewModel(Context context) {
        this.sensoresRepository = new SensoresRepository(context);
    }

    public LiveData<SensoresResponse> fetchSensores(int recorridoId, String tiempo) {
        return sensoresRepository.fetchSensores(recorridoId, tiempo);
    }

}
