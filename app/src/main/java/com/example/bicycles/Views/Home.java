package com.example.bicycles.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bicycles.R;
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
        bottomNavigation.add(new NafisBottomNavigation.Model(mas,R.drawable.mas));
        bottomNavigation.add(new NafisBottomNavigation.Model(configuracion,R.drawable.baseline_settings_24));
        bottomNavigation.add(new NafisBottomNavigation.Model(mis_bicis,R.drawable.mis_bicis));
        bottomNavigation.add(new NafisBottomNavigation.Model(mis_recorridos,R.drawable.mis_recorridos));
        bottomNavigation.add(new NafisBottomNavigation.Model(perfil,R.drawable.perfil));

        bottomNavigation.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                Toast.makeText(Home.this, "item click"+model.getId(), Toast.LENGTH_SHORT).show();
                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                String name;
                switch (model.getId()) {
                    case mas:
                        name = "mas";
                        break;
                    case configuracion:
                        name = "configuracion";
                        break;
                    case mis_bicis:
                        name = "mis_bicis";
                        break;
                    case mis_recorridos:
                        name = "mis_recorridos";
                        break;
                    case perfil:
                        name = "perfil";
                        break;
                    default:
                        name = "Unknown";
                }
                Toast.makeText(Home.this, "Mostrando: " + name, Toast.LENGTH_SHORT).show();
                return null;
            }
        });

    }
}