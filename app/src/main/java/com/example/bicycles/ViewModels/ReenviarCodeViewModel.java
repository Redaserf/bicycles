package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.ReenviarCodeRepository;

public class ReenviarCodeViewModel extends ViewModel {
    private ReenviarCodeRepository reenviarCodeRepository;
    private MutableLiveData<String> reenviarResponse;

    public ReenviarCodeViewModel(Context context) {
        this.reenviarCodeRepository = new ReenviarCodeRepository(context);
    }

    public void reenviarCodigo(String email) {
        reenviarResponse = reenviarCodeRepository.reenviarCodigo(email);
    }

    public LiveData<String> getReenviarResponse() {
        if (reenviarResponse == null) {
            reenviarResponse = new MutableLiveData<>();
        }
        return reenviarResponse;
    }

}
