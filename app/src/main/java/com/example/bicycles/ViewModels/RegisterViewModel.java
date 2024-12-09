package com.example.bicycles.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicycles.Repositories.LoginRepository;
import com.example.bicycles.Repositories.RegisterRepository;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> registerMessage = new MutableLiveData<>();
    private MutableLiveData<String> verifyResponse = new MutableLiveData<>();
    private MutableLiveData<String> loginMessage = new MutableLiveData<>();
    private RegisterRepository registerRepository;
    private LoginRepository loginRepository;

    public RegisterViewModel(Context context) {
        this.registerRepository = new RegisterRepository(context);
        this.loginRepository = new LoginRepository(context);
    }

    // Registrar usuario
    public void register(String name, String lastName, Double peso, String email, String password) {
        registerMessage = registerRepository.register(name, lastName, peso, email, password);
    }

    // Verificar código
    public void verifyCode(String email, String codigo) {
        verifyResponse = registerRepository.verifyCode(email, codigo);
    }

    // Login después de la verificación
    public void login(String email, String password) {
        loginMessage = loginRepository.login(email, password);
    }

    // Obtener mensajes de respuesta
    public LiveData<String> getRegisterMessage() {
        return registerMessage;
    }

    public LiveData<String> getVerifyResponse() {
        return verifyResponse;
    }

    public LiveData<String> getLoginMessage() {
        return loginMessage;
    }
}
