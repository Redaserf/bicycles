package com.example.bicycles.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.LogoutViewModel;
import com.example.bicycles.ViewModels.UsuarioViewModel;
import com.example.bicycles.Views.EditProfileActivity;
import com.example.bicycles.Views.login;

public class PerfilFragment extends Fragment {

    private TextView tvUsername, tvAboutMe, tvEmail, tvPeso;
    private Button btnEditProfile, btnLogout;
    private UsuarioViewModel usuarioViewModel;
    private LogoutViewModel logoutViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_perfil, container, false);

        // Inicializar vistas
        tvUsername = view.findViewById(R.id.tv_username);
        tvAboutMe = view.findViewById(R.id.tv_about_me);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPeso = view.findViewById(R.id.tv_peso);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Configurar ViewModels con Factory
        Factory factory = new Factory(requireContext());
        usuarioViewModel = new ViewModelProvider(this, factory).get(UsuarioViewModel.class);
        logoutViewModel = new ViewModelProvider(this, factory).get(LogoutViewModel.class);

        // Configurar botón para editar perfil
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Configurar botón de logout
        btnLogout.setOnClickListener(v -> realizarLogout());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        usuarioViewModel.cargarUsuario();

        usuarioViewModel.getUsuarioLiveData().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                tvUsername.setText(usuario.getNombre());
                tvAboutMe.setText(usuario.getApellido());
                tvEmail.setText(usuario.getEmail());
                tvPeso.setText(String.valueOf(usuario.getPeso()));
            } else {
                Toast.makeText(getContext(), "Error al cargar los datos del usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void realizarLogout() {
        logoutViewModel.logout();
        logoutViewModel.getLogoutMessage().observe(getViewLifecycleOwner(), mensaje -> {
            if ("Cierre de sesión exitoso.".equals(mensaje)) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();

                // Redirigir al login
                Intent intent = new Intent(requireContext(), login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
