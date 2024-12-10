package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bicycles.R;
import com.example.bicycles.Views.Fragments.ConfiguracionFragment;
import com.example.bicycles.Views.Fragments.MasFragment;
import com.example.bicycles.Views.Fragments.MisBicisFragment;
import com.example.bicycles.Views.Fragments.MisRecorridosFragment;
import com.example.bicycles.Views.Fragments.PerfilFragment;
import com.nafis.bottomnavigation.NafisBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import android.os.SystemClock; // Importar para medir el tiempo

public class Home extends AppCompatActivity implements MasFragment.OnFragmentInteractionListener {

    private NafisBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.NafisBottomNavigation);
        bottomNavigation.add(new NafisBottomNavigation.Model(1, R.drawable.mis_bicis));
        bottomNavigation.add(new NafisBottomNavigation.Model(2, R.drawable.mis_recorridos));
        bottomNavigation.add(new NafisBottomNavigation.Model(3, R.drawable.mas));
        bottomNavigation.add(new NafisBottomNavigation.Model(4, R.drawable.baseline_settings_24));
        bottomNavigation.add(new NafisBottomNavigation.Model(5, R.drawable.perfil));

        setCurrentFragment(new MasFragment());

        bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    setCurrentFragment(new MisBicisFragment());
                    break;
                case 2:
                    setCurrentFragment(new MisRecorridosFragment());
                    break;
                case 3:
                    setCurrentFragment(new MasFragment());
                    break;
                case 4:
                    setCurrentFragment(new ConfiguracionFragment());
                    break;
                case 5:
                    setCurrentFragment(new PerfilFragment());
                    break;
            }
            return null;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onHideBottomNavigation() {
        bottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void onShowBottomNavigation() {
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Elimina la notificación si la aplicación se cierra
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

}
