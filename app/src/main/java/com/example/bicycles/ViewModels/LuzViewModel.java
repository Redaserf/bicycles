package com.example.bicycles.ViewModels;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.bicycles.Repositories.LuzRepository;

public class LuzViewModel extends ViewModel {
    private final LuzRepository luzRepository;

    public LuzViewModel(Context context) {
        this.luzRepository = new LuzRepository(context);
    }

    public LiveData<Integer> cambiarEstadoLuz(Integer encender) {
        return luzRepository.cambiarEstadoLuz(encender);
    }
}
