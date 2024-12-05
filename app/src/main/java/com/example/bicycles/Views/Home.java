package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class Home extends AppCompatActivity {
    protected final int mas = 1;
    protected final int configuracion = 2;
    protected final int mis_bicis = 3;
    protected final int mis_recorridos = 4;
    protected final int perfil = 5;

    private long lastClickTimeMas = 0; // Para registrar el tiempo del último clic en "Más"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Carga un fragment inicial si es necesario


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        NafisBottomNavigation bottomNavigation = findViewById(R.id.NafisBottomNavigation);
        bottomNavigation.add(new NafisBottomNavigation.Model(mis_bicis, R.drawable.mis_bicis));
        bottomNavigation.add(new NafisBottomNavigation.Model(mis_recorridos, R.drawable.mis_recorridos));
        bottomNavigation.add(new NafisBottomNavigation.Model(mas, R.drawable.mas));
        bottomNavigation.add(new NafisBottomNavigation.Model(configuracion, R.drawable.baseline_settings_24));
        bottomNavigation.add(new NafisBottomNavigation.Model(perfil, R.drawable.perfil));

        setCurrentFragment(new MasFragment());



        // Manejar los clics del BottomNavigation
        bottomNavigation.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                switch (model.getId()) {
                    case mas:
                        long currentTime = SystemClock.elapsedRealtime();
                        if (currentTime - lastClickTimeMas < 500) { // Doble clic detectado (menos de 500ms)
                            openMasActivity(); // Redirigir a la nueva vista
                        } else {
                            setCurrentFragment(new MasFragment()); // Cargar el fragmento normalmente
                        }
                        lastClickTimeMas = currentTime; // Actualizar el último tiempo de clic
                        break;
                    case configuracion:
                        setCurrentFragment(new ConfiguracionFragment());
                        break;
                    case mis_bicis:
                        setCurrentFragment(new MisBicisFragment());
                        break;
                    case mis_recorridos:
                        setCurrentFragment(new MisRecorridosFragment());
                        break;
                    case perfil:
                        setCurrentFragment(new PerfilFragment());
                        break;
                    default:
                        Toast.makeText(Home.this, "Opción desconocida", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
    }

    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openMasActivity() {
        setCurrentFragment(new MasFragment());
    }


    private void setCurrentFragment(Fragment fragment) {
        Toast.makeText(this, "Cambiando al fragment: " + fragment.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
