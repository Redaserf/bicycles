package com.example.bicycles.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.R;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.ViewModels.BicicletaViewModel;

public class Agregar_bici extends AppCompatActivity {

    private Button agregar_bici;
    public EditText nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_bici);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        nombre = findViewById(R.id.nombre);
//        agregar_bici = findViewById(R.id.agg_bici);
//        Factory factory = new Factory(this);
//        BicicletaViewModel viewModel = new ViewModelProvider(this, factory)
//        .get(BicicletaViewModel.class);


//        viewModel.getBicicletaResponse().observe(this, new Observer<BicicletaResponse>() {
//            @Override
//            public void onChanged(BicicletaResponse bicicletaResponse) {
//                //No se
//            }
//        });


        //Logica para agregar bici OBSERVER y demas
//        agregar_bici.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str_nombre = nombre.getText().toString();
//                if(str_nombre.isEmpty()){
//                    Toast.makeText(Agregar_bici.this, "Es requerido agregar el nombre", Toast.LENGTH_SHORT).show();
//                }else{
//                    viewModel.addBicicleta(str_nombre);
//                }
//            }
//        });

    }
}