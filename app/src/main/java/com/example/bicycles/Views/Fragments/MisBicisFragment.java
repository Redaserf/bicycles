package com.example.bicycles.Views.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Adapters.MisBicisAdapter;
import com.example.bicycles.Factory.Factory;
import android.Manifest;
import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.BicicletaRequest;
import com.example.bicycles.R;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.EditarBicicletaResponse;
import com.example.bicycles.Responses.EliminarBicicletaResponse;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.ViewModels.BicicletaViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.Views.Agregar_bici;
import com.example.bicycles.Views.EliminarInterfaz;
import com.example.bicycles.Views.OnImageResultListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MisBicisFragment extends Fragment implements EliminarInterfaz {
    private static final int REQUEST_IMAGE_CAPTURE = 1; // Código para la cámara
    private static final int REQUEST_IMAGE_PICK = 2;   // Código para la galería


    public RecyclerView recyclerMisBicis;
    public List<Bicicleta> bicicletas = new ArrayList<>();
    public Button agregar_bici;
    //mostrar imagen y convertir
//    private MultipartBody.Part imagen = null;

    private String nombre;

    private ImageView prevImagen;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    //constante del permiso de la camara
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    public MisBicisAdapter adapter;
    public MisBicisViewModel misBicisViewModel;

    public BicicletaViewModel viewModel;
    public AlertDialog dialog;

    public Button btnEditar;
    public Button btnAgregar;
    public ProgressDialog progressDialog;
    public SearchView buscar;


    public View onCreateView(@NonNull
     LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_bicis, container, false);
        agregar_bici = view.findViewById(R.id.agregar_bici);
        buscar = view.findViewById(R.id.edTxtBuscar);

        agregar_bici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imagen = null;
                mostrarDialogoAgregarBici();
            }
        });

        Context context = requireContext();
        Factory factory = new Factory(context);
//        if (context != null) {
//            Log.e("Fragment", "Context exists");
//        } else {
//            Log.e("Fragment", "Context is null");
//        }
        misBicisViewModel = new ViewModelProvider(
                this, factory).get(MisBicisViewModel.class);

        adapter = new MisBicisAdapter(bicicletas, requireContext(), requireActivity(), this);
        recyclerMisBicis = view.findViewById(R.id.recycler_mis_bicis);
        recyclerMisBicis.setAdapter(adapter);
        recyclerMisBicis.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerMisBicis.setHasFixedSize(true);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Cargando bicicletas...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ImageView searchIcon = buscar.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setVisibility(View.GONE);
        buscar.setIconified(false);

        buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        buscar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Retornar true significa que se consume el evento y NO se cierra
                // Si quieres que la "X" solo limpie el texto, hazlo aquí manualmente
                buscar.setQuery("", false);
                return true;
            }
        });


        misBicisViewModel.getBicicletasLiveData().observe(getViewLifecycleOwner(), new Observer<MisBicicletasResponse>() {
            @Override
            public void onChanged(MisBicicletasResponse misBicicletasResponse) {
                if (misBicicletasResponse != null && misBicicletasResponse.getBicicletas() != null) {
                    adapter.actualizarLista(misBicicletasResponse.getBicicletas());
                    bicicletas = misBicicletasResponse.getBicicletas();
                    Log.d("DEBUG","Se actualizo el adapter con las nuevas bicicletas");
                } else {
                    Log.e("ERROR", "Respuesta nula o lista de bicicletas vacía.");
                }
                progressDialog.dismiss();
            }
        });

        misBicisViewModel.fetchBicicletas();


        Factory factory1 = new Factory(requireContext());
        viewModel = new ViewModelProvider(this, factory1).get(BicicletaViewModel.class);
        viewModel.getEditarResponse().observe(getViewLifecycleOwner(), new Observer<EditarBicicletaResponse>() {
            @Override
            public void onChanged(EditarBicicletaResponse editarBicicletaResponse) {
                if (editarBicicletaResponse != null) {
                    Log.d("DEVBUG", "El adapter se actualizó con el elemento que cambió");
                    int position = findBicicletaPosition(editarBicicletaResponse.getBicicleta().getId());
                    if (position != -1) {

                        adapter.editarElemento(position,
                                editarBicicletaResponse.getBicicleta().getNombre());

                        progressDialog.dismiss();
                        btnEditar.setEnabled(true);
                    }
                } else {
                    Log.e("DEVBUG", "Error: La respuesta de la edición es nula.");
                }
            }
        });

        viewModel.getBicicletaCreated().observe(getViewLifecycleOwner(), new Observer<BicicletaResponse>() {
            @Override
            public void onChanged(BicicletaResponse bicicletaResponse) {
                if(bicicletaResponse != null){
                    if(bicicletaResponse.getBicicleta() != null){
                        bicicletas.add(bicicletaResponse.getBicicleta());
                        int position = findBicicletaPosition(bicicletaResponse.getBicicleta().getId());
                        if(position == -1){
                            Log.d("DEBUG", "No se enconntro la bici");
                        }else{
                            adapter.insertarElemento(position, bicicletaResponse.getBicicleta());
                        }
                    }else{
                        Log.d("DEBUG", "La bicicleta viene nula");
                        Toast.makeText(requireContext(), "No se pudo crear la bicicleta", Toast.LENGTH_LONG).show();

                    }
                }
                btnAgregar.setEnabled(true);
                progressDialog.dismiss();
            }
        });

        viewModel.getElimnarBicicleta().observe(getViewLifecycleOwner(), new Observer<EliminarBicicletaResponse>() {
            @Override
            public void onChanged(EliminarBicicletaResponse eliminarBicicletaResponse) {

                    if(eliminarBicicletaResponse.getBicicleta() != null){
                        int position = findBicicletaPosition(eliminarBicicletaResponse.getBicicleta().getId());
                        if(position != -1) {
                            adapter.eliminarElemento(position);

                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, new MasFragment())
                                    .commit();
                        }else{
                         Toast.makeText(requireContext(), "No se encontro la bicicleta por eliminar", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(requireContext(), "No se pudo eliminar la bicicleta", Toast.LENGTH_SHORT).show();
                    }
                progressDialog.dismiss();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MisBicisFragment())
                        .commit();


            }
        });
        return view;
    }

    private int findBicicletaPosition(int id) {
        for (int i = 0; i < bicicletas.size(); i++) {
            if (bicicletas.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void mostrarDialogoAgregarBici() {


        LayoutInflater inflater = getLayoutInflater();
        View vistaDialogo = inflater.inflate(R.layout.agregar_bici_dialog, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo);

//        prevImagen = vistaDialogo.findViewById(R.id.prev_imagen);
        EditText edtxtNombre = vistaDialogo.findViewById(R.id.edtxt_nombre);
        btnAgregar = vistaDialogo.findViewById(R.id.btnAgregar);
        Button btnCancel = vistaDialogo.findViewById(R.id.btnCancel);

//        prevImagen.setOnClickListener(v -> {
//            showImagePickerDialog();
//        });
        dialog = builder.create();


        btnAgregar.setOnClickListener(v -> {
            nombre = edtxtNombre.getText().toString().trim();
            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingresa un nombre.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nombre.length() > 60) {
                Toast.makeText(requireContext(), "El nombre no debe superar los 60 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            Factory factory = new Factory(requireContext());
            viewModel = new ViewModelProvider(this, factory).get(BicicletaViewModel.class);
            BicicletaRequest bicicletaRequest = new BicicletaRequest(nombre);
            viewModel.addBicicleta(bicicletaRequest);

            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Agregando bicicleta...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            dialog.dismiss();
        });




        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }




    private void mostrarEditarDialog(Bicicleta bici, int position){
//        imagen = null;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.editar_bici, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        prevImagen = view.findViewById(R.id.imagen_edit);
        EditText viewNombre = view.findViewById(R.id.nombre_edit);
        btnEditar = view.findViewById(R.id.btnEditar);
        Button btnCancel = view.findViewById(R.id.btnCancel);


        Picasso.get().load(R.drawable.bicicletatarjeta).resize(1200, 720)
                .error(R.drawable.bicicletatarjeta).into(prevImagen);
        viewNombre.setText(bici.getNombre());



        dialog = builder.create();

        dialog.show();


        File cacheDir = requireContext().getApplicationContext().getCacheDir();
        File imageFile = new File(cacheDir, "cached_image.jpg");

//        prevImagen.setOnClickListener(v -> {
//            showImagePickerDialog();
//        });


        btnEditar.setOnClickListener(v -> {
            nombre = viewNombre.getText().toString().trim();
            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingresa un nombre.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nombre.length() > 60) {
                Toast.makeText(requireContext(), "El nombre no debe superar los 60 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            Factory factory = new Factory(requireContext());
            viewModel = new ViewModelProvider(this, factory).get(BicicletaViewModel.class);

            BicicletaRequest request = new BicicletaRequest(nombre);
            viewModel.editarBicicleta(bici.getId(), request);
            dialog.dismiss();

            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Editando bicicleta...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        });


    }

    private void mostrarDialogEliminar(Bicicleta bici, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.eliminar_bici_dialog, null);
        bottomSheetDialog.setContentView(view);

        TextView nombre = view.findViewById(R.id.nombre);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnEditar = view.findViewById(R.id.btnEditar);
        ImageView imagenEliminar = view.findViewById(R.id.imagen);

        nombre.setText(bici.getNombre());
        Picasso.get().load(R.drawable.bicicletatarjeta)
                .resize(1200, 720)
                .error(R.drawable.bicicletatarjeta).into(imagenEliminar);
        AppCompatActivity activity = (AppCompatActivity) requireContext();

        btnDelete.setOnClickListener(v -> {
            if (requireContext() instanceof AppCompatActivity) {

                viewModel.eliminarBicicleta(bici.getId());
                bottomSheetDialog.dismiss();



                progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage("Eliminando bicicleta...");
                progressDialog.setCancelable(false);
                progressDialog.show();

            } else {
                Toast.makeText(requireContext(), "No se puede crear el ViewModelProvider con este contexto", Toast.LENGTH_SHORT).show();
            }

            bottomSheetDialog.dismiss();
            Toast.makeText(requireContext(), "Bicicleta eliminada", Toast.LENGTH_SHORT).show();
        });

        btnEditar.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Log.d("DEBUG", "Se mostro el modal de editar");
            mostrarEditarDialog(bici, position);
        });

        btnCancel.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    @Override
    public void eliminarBicicleta(Bicicleta bici, int position) {

        mostrarDialogEliminar(bici, position);
    }
}
