package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.RegisterRepository;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> registerMessage;
    private RegisterRepository registerRepository;

    public RegisterViewModel(Context context) {
        this.registerRepository = new RegisterRepository(context);
    }

    public void register(String name, String lastName, String email, String password, Context context) {

        registerMessage = registerRepository.register(name, lastName, email, password, context);

    }

    public LiveData<String> getRegisterMessage() {
        return registerMessage;
    }

}
