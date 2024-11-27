package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.LoginRepository;

public class LoginViewModel extends ViewModel {

    private LoginRepository loginRepository;
    private MutableLiveData<String> loginMessage;

    public LoginViewModel(Context context) {
        this.loginRepository = new LoginRepository(context);
    }

    public void login(String email, String password, Context context) {
        loginMessage = loginRepository.login(email, password, context);
    }

    public LiveData<String> getLoginMessage() {
        return loginMessage;
    }

}
