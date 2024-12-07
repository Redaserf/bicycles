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

public class MisBicisDialogAdapter extends RecyclerView.Adapter<MisBicisDialogAdapter.ViewHolder> {
    private List<Bicicleta> bicicletas;
    private OnBicicletaClickListener listener; // Listener para manejar clics en el diálogo.

    public MisBicisDialogAdapter(List<Bicicleta> bicicletas, OnBicicletaClickListener listener) {
        this.bicicletas = bicicletas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bici_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(bicicletas.get(position));
    }

    @Override
    public int getItemCount() {
        return bicicletas.size();
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
