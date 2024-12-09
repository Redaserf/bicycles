package com.example.bicycles.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;
import com.example.bicycles.Views.OnBicicletaClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MisBicisDialogAdapter extends RecyclerView.Adapter<MisBicisDialogAdapter.ViewHolder> {
    private List<Bicicleta> bicicletas;
    private List<Bicicleta> filteredBicicletas;
    private OnBicicletaClickListener listener; // Listener para manejar clics en el diálogo.

    public MisBicisDialogAdapter(List<Bicicleta> bicicletas, OnBicicletaClickListener listener) {
        this.bicicletas = new ArrayList<>(bicicletas);
        this.filteredBicicletas = new ArrayList<>(bicicletas);
        this.listener = listener;
    }

    public void actualizarLista(List<Bicicleta> bicicletas){
        this.bicicletas.clear();
        this.bicicletas.addAll(bicicletas);

        this.filteredBicicletas.clear();
        this.filteredBicicletas.addAll(bicicletas);
        notifyDataSetChanged();
        Log.d("DEBUG", "Se notifico al adaptador que las bicicletas se actualizaron");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bici_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filteredBicicletas.get(position), position);
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

    @Override
    public int getItemCount() {
        return filteredBicicletas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombreBicicleta;
        private ImageView imagen;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreBicicleta = itemView.findViewById(R.id.nombre_bicicleta);
            imagen = itemView.findViewById(R.id.imagen_bicicleta);

            itemView.setOnClickListener(this);
        }

        public void bind(Bicicleta bicicleta, int position) {
            this.position = position;
            nombreBicicleta.setText(bicicleta.getNombre());
            Picasso.get().load(bicicleta.getImagen()).resize(1200, 720)
                    .error(R.drawable.bicicletatarjeta).into(imagen);

            Log.d("DEBUG", "Se esta llenando el recycler view");
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onBicicletaClick(bicicletas.get(getAdapterPosition()));
            }
        }
    }

}