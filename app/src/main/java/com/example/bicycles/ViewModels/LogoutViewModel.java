package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.LogoutRepository;

public class LogoutViewModel extends ViewModel {
    private final LogoutRepository logoutRepository;
    private MutableLiveData<String> logoutMessage;

    public LogoutViewModel(Context context) {
        this.logoutRepository = new LogoutRepository(context);
    }

    public void logout() {
        if (logoutMessage == null) {
            logoutMessage = new MutableLiveData<>();
        }
        logoutMessage = logoutRepository.logout();
    }

    public LiveData<String> getLogoutMessage() {
        return logoutMessage;
    }
}
