package com.example.bicycles.Factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.ViewModels.LoginViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.ViewModels.RegisterViewModel;

public class Factory implements ViewModelProvider.Factory {
    private final Context context;

    public Factory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MisBicisViewModel.class)) {
            return (T) new MisBicisViewModel(context);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(context);
        } else if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(context);
        }  //A qui ir agregando los viewModels

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
