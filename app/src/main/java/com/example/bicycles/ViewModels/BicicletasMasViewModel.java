package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Repositories.BicicletasMasRepository;

import java.util.List;

public class BicicletasMasViewModel extends ViewModel {

    private final MutableLiveData<List<Bicicleta>> bicicletasLiveData = new MutableLiveData<>();
    private BicicletasMasRepository repository;

    public BicicletasMasViewModel(Context context) {
        repository = new BicicletasMasRepository(context);
    }

    public LiveData<List<Bicicleta>> getBicicletasLiveData() {
        return bicicletasLiveData;
    }

    public void fetchBicicletas() {
        repository.fetchBicicletas().observeForever(bicicletasLiveData::setValue);
    }
}

