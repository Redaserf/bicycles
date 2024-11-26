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

public class Home extends AppCompatActivity {
    protected final int mas=1;
    protected final int configuracion=2;
    protected final int mis_bicis=3;
    protected final int mis_recorridos=4;
    protected final int perfil=5;

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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        NafisBottomNavigation bottomNavigation = findViewById(R.id.NafisBottomNavigation);
        bottomNavigation.add(new NafisBottomNavigation.Model(mis_bicis,R.drawable.mis_bicis));
        bottomNavigation.add(new NafisBottomNavigation.Model(mis_recorridos,R.drawable.mis_recorridos));
        bottomNavigation.add(new NafisBottomNavigation.Model(mas,R.drawable.mas));
        bottomNavigation.add(new NafisBottomNavigation.Model(configuracion,R.drawable.baseline_settings_24));
        bottomNavigation.add(new NafisBottomNavigation.Model(perfil,R.drawable.perfil));

        bottomNavigation.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                Toast.makeText(Home.this, "item click"+model.getId(), Toast.LENGTH_SHORT).show();
                return null;
            }
        });

        bottomNavigation.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                switch (model.getId()) {
                    case mas:
                        setCurrentFragment(new MasFragment());
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

    private void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}