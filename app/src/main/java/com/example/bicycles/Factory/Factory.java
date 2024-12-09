package com.example.bicycles.Factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Responses.VerifyCodeResponse;
import com.example.bicycles.ViewModels.AllRecorridosUsuarioViewModel;
import com.example.bicycles.ViewModels.BicicletaViewModel;
import com.example.bicycles.ViewModels.BicicletasMasViewModel;
import com.example.bicycles.ViewModels.EliminarVelocidadViewModel;
import com.example.bicycles.ViewModels.LoginViewModel;
import com.example.bicycles.ViewModels.LogoutViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.ViewModels.RecorridoInicioViewModel;
import com.example.bicycles.ViewModels.ReenviarCodeViewModel;
import com.example.bicycles.ViewModels.RegisterViewModel;
import com.example.bicycles.ViewModels.SensoresViewModel;
import com.example.bicycles.ViewModels.UsuarioEditViewModel;
import com.example.bicycles.ViewModels.UsuarioViewModel;
import com.example.bicycles.ViewModels.VerifyCodeViewModel;

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
        } else if (modelClass.isAssignableFrom(SensoresViewModel.class)) {
            return (T) new SensoresViewModel(context);
        } else if (modelClass.isAssignableFrom(UsuarioEditViewModel.class)) {
            return (T) new UsuarioEditViewModel(context);
        } else if (modelClass.isAssignableFrom(UsuarioViewModel.class)) {
            return (T) new UsuarioViewModel(context);
        } else if (modelClass.isAssignableFrom(AllRecorridosUsuarioViewModel.class)) {
            return (T) new AllRecorridosUsuarioViewModel(context);
        } else if (modelClass.isAssignableFrom(LogoutViewModel.class)) {
            return (T) new LogoutViewModel(context);
        } else if (modelClass.isAssignableFrom(BicicletaViewModel.class)) {
            return (T) new BicicletaViewModel(context);
        } else if (modelClass.isAssignableFrom(RecorridoInicioViewModel.class)) {
            return (T) new RecorridoInicioViewModel(context);
        } else if (modelClass.isAssignableFrom(EliminarVelocidadViewModel.class)) {
            return (T) new EliminarVelocidadViewModel(context);
        } else if (modelClass.isAssignableFrom(VerifyCodeViewModel.class)) {
            return (T) new VerifyCodeViewModel(context);
        } else if (modelClass.isAssignableFrom(ReenviarCodeViewModel.class)) {
            return (T) new ReenviarCodeViewModel(context);
        } else if (modelClass.isAssignableFrom(BicicletasMasViewModel.class)) {
            return (T) new BicicletasMasViewModel(context);
        }
        //A qui ir agregando los viewModels

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
