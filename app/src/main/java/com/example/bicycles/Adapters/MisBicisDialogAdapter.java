package com.example.bicycles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.R;

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

    public  void actualizarLista(List<Bicicleta> bicicletas){
        this.bicicletas.clear();
        this.bicicletas.addAll(bicicletas);

        this.filteredBicicletas.clear();
        this.filteredBicicletas.addAll(bicicletas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bici_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filteredBicicletas.get(position));
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreBicicleta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreBicicleta = itemView.findViewById(R.id.nombre_bicicleta);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBicicletaClick(bicicletas.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Bicicleta bicicleta) {
            nombreBicicleta.setText(bicicleta.getNombre());
        }
    }

    public interface OnBicicletaClickListener {
        void onBicicletaClick(Bicicleta bicicleta);
    }
}
