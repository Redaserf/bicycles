package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.VerifyCodeRepository;

public class VerifyCodeViewModel extends ViewModel {

    private VerifyCodeRepository verifyCodeRepository;
    private MutableLiveData<String> verifyResponse;

    public VerifyCodeViewModel(Context context) {
        this.verifyCodeRepository = new VerifyCodeRepository(context);
    }

    public void verifyCode(String email, String code) {
        verifyResponse = verifyCodeRepository.verifyCode(email, code);
    }

    public LiveData<String> getVerifyResponse() {
        return verifyResponse;
    }
}
