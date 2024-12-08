package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.EliminarVelocidadRepository;
import com.example.bicycles.Responses.EliminarVelocidadResponse;

public class EliminarVelocidadViewModel extends ViewModel {
    private EliminarVelocidadRepository eliminarVelocidadRepository;

    public EliminarVelocidadViewModel(Context context) {
        this.eliminarVelocidadRepository = new EliminarVelocidadRepository(context);
    }

    public LiveData<EliminarVelocidadResponse> eliminarVelocidades(int recorridoId) {
        return eliminarVelocidadRepository.eliminarVelocidades(recorridoId);
    }
}
