package com.example.bicycles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;

import java.util.List;

public class MisBicisAdapter extends RecyclerView.Adapter<MisBicisAdapter.ViewHolder> {

    private List<Bicicleta> bicicletas;
    private OnBicicletaClickListener listener;

    // Constructor
    public MisBicisAdapter(List<Bicicleta> bicicletas, OnBicicletaClickListener listener) {
        this.bicicletas = bicicletas;
        this.listener = listener;
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

    public interface OnBicicletaClickListener {
        void onBicicletaClick(Bicicleta bicicleta);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.nombre_bicicleta);
            itemView.setOnClickListener(this);
        }

        public void bind(Bicicleta bicicleta) {
            nombre.setText(bicicleta.getNombre());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onBicicletaClick(bicicletas.get(getAdapterPosition()));
            }
        }
    }
}
