package com.example.bicycles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;

import java.util.List;

public class MisBicisAdapter extends RecyclerView.Adapter<MisBicisAdapter.ViewHolder> {
    private List<Bicicleta> bicicletas;

    public MisBicisAdapter(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
    }

    @NonNull
    @Override
    public MisBicisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bici, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisBicisAdapter.ViewHolder holder, int position) {
        holder.bind(bicicletas.get(position));
    }

    @Override
    public int getItemCount() {
        return bicicletas.size();
    }

    // Método para actualizar la lista de bicicletas y refrescar el RecyclerView
    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.nombre_bicicleta);
        }

        public void bind(Bicicleta bicicleta) {
            nombre.setText(bicicleta.getNombre());
        }
    }
}
