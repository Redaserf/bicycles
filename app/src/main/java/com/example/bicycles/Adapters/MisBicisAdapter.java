package com.example.bicycles.Adapters;

import static android.app.Activity.RESULT_OK;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContentProviderCompat.requireContext;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Factory.Factory;
import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;
import com.example.bicycles.ViewModels.BicicletaViewModel;
import com.example.bicycles.ViewModels.MisBicisViewModel;
import com.example.bicycles.Views.EliminarInterfaz;
import com.example.bicycles.Views.Fragments.MisBicisFragment;
import com.example.bicycles.Views.OnImageResultListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MisBicisAdapter extends RecyclerView.Adapter<MisBicisAdapter.ViewHolder> {
    public Activity activity;
    public Context context;
    private List<Bicicleta> bicicletas;

    private List<Bicicleta> filteredBicicletas;
    public MultipartBody.Part imagenEdit;

    public RequestBody nombreEdit;
    public EliminarInterfaz listener;

    public MisBicisAdapter(List<Bicicleta> bicicletas, Context context, Activity activity, EliminarInterfaz listener){
        this(bicicletas, context, activity, null, null, listener);
    }

    public MisBicisAdapter(List<Bicicleta> bicicletas, Context context, Activity activity,
                           MultipartBody.Part imagen, RequestBody nombre, EliminarInterfaz listener) {
        this.activity = activity;
        this.context = context;
        this.bicicletas = new ArrayList<>(bicicletas);
        this.filteredBicicletas = new ArrayList<>(bicicletas);
        this.imagenEdit = imagen;
        this.nombreEdit = nombre;
        this.listener = listener;
    }

    public void eliminarElemento(int position) {
        this.bicicletas.remove(position);

        notifyItemRemoved(position);//Puede fallar al eliminar una cita se duplica esa misma y al querer volver a elimnarla truena
//        notifyItemRemoved(position); // Actualiza las posiciones del resto de elementos

    }

    public void insertarElemento(int position, Bicicleta nuevaBicicleta) {
        if (nuevaBicicleta != null) {
//            bicicletas.add(position ,nuevaBicicleta);
            notifyItemInserted(position);
            Log.d("DEBUG", "Elemento insertado en posición " + position + ": " + nuevaBicicleta.getNombre());
        }
    }
    public void editarElemento(int position, String nuevoNombre, String nuevaImagen) {
        Bicicleta bici = bicicletas.get(position);
        bici.setNombre(nuevoNombre);
        if (nuevaImagen != null) {
            bici.setImagen(nuevaImagen);
        }


        bicicletas.remove(position);
        bicicletas.add(position, bici);

        notifyItemChanged(position);
        Log.d("DEBUG", "Se notificó al adaptador que cambió un elemento en la posición: " + position);
    }


    public  void actualizarLista(List<Bicicleta> bicicletas){
        this.bicicletas.clear();
        this.bicicletas.addAll(bicicletas);

        this.filteredBicicletas.clear();
        this.filteredBicicletas.addAll(bicicletas);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MisBicisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bici, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MisBicisAdapter.ViewHolder holder, int position) {
        Bicicleta bicicleta = filteredBicicletas.get(position);

        holder.bind(bicicleta, position);
    }

    @Override
    public int getItemCount() {
        return filteredBicicletas.size();
    }

    public void filter(String text) {
        filteredBicicletas.clear();
        if (text.isEmpty()) {
            filteredBicicletas.addAll(bicicletas);
        } else {
            text = text.toLowerCase();
            for (Bicicleta item : bicicletas) {
                if (item.getNombre().toLowerCase().contains(text)) {
                    filteredBicicletas.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

//    public void setBicicletas(List<Bicicleta> bicicletas) {
//        this.bicicletas = bicicletas;
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nombre;
        public ConstraintLayout layout;
        public ImageView imagen;
        public ImageButton menu;
        public Context context;
        public Bicicleta bici;
        private int position;

        //maybew para cuando le de click a cualquier parte de el item que le pueda editar el nombre

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.nombre = itemView.findViewById(R.id.nombre_bicicleta);
            this.imagen = itemView.findViewById(R.id.imagen_bicicleta);
            this.menu = itemView.findViewById(R.id.menu_opciones);
            menu.setOnClickListener(this);
        }


        public void bind(Bicicleta bicicleta, int position) {
//            Log.d("MisBicisAdapter", "Binding bicicleta: " + bicicleta.getNombre());
            this.bici = bicicleta;
            this.position = position;
            nombre.setText(bicicleta.getNombre());
            Picasso.get().load(bici.getImagen())
                    .resize(900, 900)
                    .into(imagen);
            menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == menu.getId()){
                listener.eliminarBicicleta(bici, position);
            }

        }


    }


}
