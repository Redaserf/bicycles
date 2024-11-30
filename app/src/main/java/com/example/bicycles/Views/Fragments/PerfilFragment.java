package com.example.bicycles.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bicycles.R;
import com.example.bicycles.Views.EditProfileActivity;
import com.example.bicycles.Views.login;

public class PerfilFragment extends Fragment {

    private TextView tvUsername, tvAboutMe;
    private Button btnEditProfile;
    private LinearLayout btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.activity_perfil, container, false);

        // Inicializar vistas
        tvUsername = view.findViewById(R.id.tv_username);
        tvAboutMe = view.findViewById(R.id.tv_about_me);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Configurar valores iniciales
        tvUsername.setText("Pedro"); // Cambiar por datos reales si los tienes
        tvAboutMe.setText("Escribe algo interesante sobre ti");

        // Configurar botón para editar perfil
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            intent.putExtra("username", tvUsername.getText().toString());
            intent.putExtra("about_me", tvAboutMe.getText().toString());
            startActivityForResult(intent, 1); // Código de solicitud 1
        });

        // Configurar botón de logout
        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        // Lógica de cierre de sesión (por ejemplo, limpiar datos de SharedPreferences o variables locales)
        Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

        // Redirigir al login
        Intent intent = new Intent(getContext(), login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            // Recuperar los datos actualizados desde EditProfileActivity
            String updatedUsername = data.getStringExtra("updated_username");
            String updatedAboutMe = data.getStringExtra("updated_about_me");

            // Actualizar vistas en el perfil
            if (updatedUsername != null) {
                tvUsername.setText(updatedUsername);
            }
            if (updatedAboutMe != null) {
                tvAboutMe.setText(updatedAboutMe);
            }
        }
    }
}
