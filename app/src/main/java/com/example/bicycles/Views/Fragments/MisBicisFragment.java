package com.example.bicycles.Views.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private List<Bicicleta> listaAnterior = new ArrayList<>();


    //mostrar imagen y convertir
    private MultipartBody.Part imagen;

    private String nombre;

    private ImageView prevImagen;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    //constante del permiso de la camara
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private BicicletaRequest request;

    private Uri imageUri;
    public MisBicisAdapter adapter;
    public MisBicisViewModel misBicisViewModel;

    public List<Bicicleta> bicicletaList;
    public BicicletaViewModel viewModel;
    public AlertDialog dialog;

    public Button btnEditar;
    public Button btnAgregar;
    public ProgressDialog progressDialog;




    public View onCreateView(@NonNull
     LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mis_bicis, container, false);
        agregar_bici = view.findViewById(R.id.agregar_bici);

        agregar_bici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarBici();
            }
        });


        Context context = requireContext();
        Factory factory = new Factory(context);
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


        misBicisViewModel.getBicicletasLiveData().observe(getViewLifecycleOwner(), new Observer<List<Bicicleta>>() {
            @Override
            public void onChanged(List<Bicicleta> bicicletas) {
                adapter.actualizarLista(bicicletas);
                bicicletaList = bicicletas;
                progressDialog.dismiss();

                Log.d("DEBUG", "Cargaron las bicicletas del recycler view");
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
                                editarBicicletaResponse.getBicicleta().getNombre(),
                                editarBicicletaResponse.getBicicleta().getImagen());

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

                  int position = findBicicletaPosition(eliminarBicicletaResponse.getBicicleta().getId());
                  if(position != -1){
                    adapter.eliminarElemento(position);
                    progressDialog.dismiss();
                  }else{
                      Toast.makeText(requireContext(), "No se encontro la bicicleta por eliminar", Toast.LENGTH_LONG).show();
                  }

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

        prevImagen = vistaDialogo.findViewById(R.id.prev_imagen);
        EditText edtxtNombre = vistaDialogo.findViewById(R.id.edtxt_nombre);
        btnAgregar = vistaDialogo.findViewById(R.id.btnAgregar);
        Button btnCancel = vistaDialogo.findViewById(R.id.btnCancel);

        prevImagen.setOnClickListener(v -> {
            showImagePickerDialog();
        });
        dialog = builder.create();


        btnAgregar.setOnClickListener(v -> {
            btnAgregar.setEnabled(false);
            nombre = edtxtNombre.getText().toString().trim();
            if (!nombre.isEmpty()) {

                if(imagen != null){

                    RequestBody nombreBody = RequestBody.create(
                            MediaType.parse("text/plain"),
                            nombre
                    );


                    Factory factory = new Factory(requireContext());
                    viewModel = new ViewModelProvider(this, factory).get(BicicletaViewModel.class);
                    viewModel.addBicicleta(imagen, nombreBody);

                    progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Agregando bicicleta...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    dialog.dismiss();

                }else{
                    Toast.makeText(requireContext(), "Selecciona una imagen válida", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(requireContext(), "Por favor, ingresa un nombre.", Toast.LENGTH_SHORT).show();
            }
        });



        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showImagePickerDialog() {
        String[] opciones = {"Tomar Foto", "Seleccionar en Galería", "Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccionar Imagen");
        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {

                if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    abrirCamara();
                }
            } else if (which == 1) {

                Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGaleria, REQUEST_GALLERY);
            } else {
                dialog.dismiss();
            }
        });

        builder.show();
    }
    private void abrirCamara() {
        try {
            File cacheDir = requireContext().getApplicationContext().getCacheDir();
            File imageFile = new File(cacheDir, "cached_image.jpg");

            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }

            Uri imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.bicycles.fileprovider",
                    imageFile
            );

            Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(intentCamara, REQUEST_CAMERA);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al preparar la cámara.", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                File cacheDir = requireContext().getApplicationContext().getCacheDir();
                File imageFile = new File(cacheDir, "cached_image.jpg");

                if (imageFile.exists()) {
                    Uri imageUri = Uri.fromFile(imageFile);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                        saveImageToCache(bitmap);
                        prevImagen.setImageBitmap(bitmap);
                        Toast.makeText(requireContext(), "Foto tomada y mostrada", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "No se encontró la imagen", Toast.LENGTH_SHORT).show();
                }


            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {

                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImage);
                    saveImageToCache(bitmap);
                    prevImagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(requireContext(), "No se pudo insertar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToCache(Bitmap bitmap) {
        try {
            File cacheDir = requireContext().getApplicationContext().getCacheDir();
            File imageFile = new File(cacheDir, "cached_image.jpg");

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            convertToMultipart(imageFile);

            Log.d("DEBUG", "Se guardo la imagen en el cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void convertToMultipart(File file) {
        RequestBody requestFile =
                RequestBody.create(MultipartBody.FORM, file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

        imagen = body;
    }

    private void mostrarEditarDialog(Bicicleta bici, int position){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.editar_bici, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        prevImagen = view.findViewById(R.id.imagen_edit);
        EditText viewNombre = view.findViewById(R.id.nombre_edit);
        btnEditar = view.findViewById(R.id.btnEditar);
        Button btnCancel = view.findViewById(R.id.btnCancel);


        Picasso.get().load(bici.getImagen()).into(prevImagen);
        viewNombre.setText(bici.getNombre());

        dialog = builder.create();

        dialog.show();


        File cacheDir = requireContext().getApplicationContext().getCacheDir();
        File imageFile = new File(cacheDir, "cached_image.jpg");

        prevImagen.setOnClickListener(v -> {
            showImagePickerDialog();
        });



        btnEditar.setOnClickListener(v -> {
            btnEditar.setEnabled(false);
            nombre = viewNombre.getText().toString();
            if(!nombre.isEmpty()  || imagen != null){
                Factory factory = new Factory(requireContext());
                viewModel = new ViewModelProvider(this,factory).get(BicicletaViewModel.class);

                RequestBody nombreBody = RequestBody.create(
                        MediaType.parse("text/plain"),
                        nombre
                );

                viewModel.editarBicicleta(bici.getId(), imagen, nombreBody);
                dialog.dismiss();

                progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage("Editando bicicleta...");
                progressDialog.setCancelable(false);
                progressDialog.show();
//                adapter.editarElemento(position, nombre, null);


            }else{
                Toast.makeText(requireContext(), "Verifique los campos correctamente", Toast.LENGTH_SHORT).show();
            }

        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
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
        Picasso.get().load(bici.getImagen()).into(imagenEliminar);
        AppCompatActivity activity = (AppCompatActivity) requireContext();

        btnDelete.setOnClickListener(v -> {
            if (requireContext() instanceof AppCompatActivity) {

                viewModel.eliminarBicicleta(bici.getId());
                bottomSheetDialog.dismiss();

                progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage("Eliminando bicicleta...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Toast.makeText(requireContext(), "Bicicleta eliminada", Toast.LENGTH_SHORT).show();
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
