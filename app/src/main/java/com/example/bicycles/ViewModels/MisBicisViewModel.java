package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Repositories.MisBicicletasRepository;

import java.util.List;

public class MisBicisViewModel extends ViewModel {
    private MisBicicletasRepository repository;
    private MutableLiveData<List<Bicicleta>> bicicletas;

    public MisBicisViewModel(Context context) {
        repository = new MisBicicletasRepository(context);
    }

    public LiveData<List<Bicicleta>> getMisBicicletas() {
        return bicicletas;
    }

    public void fetchMisBicicletas() {
        if (bicicletas == null) {
            bicicletas = new MutableLiveData<>();
        }
        bicicletas = (MutableLiveData<List<Bicicleta>>) repository.getMisBicicletas();
    }

}