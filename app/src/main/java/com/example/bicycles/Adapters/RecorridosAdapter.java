package com.example.bicycles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.R;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;

import java.util.ArrayList;
import java.util.List;

public class RecorridosAdapter extends RecyclerView.Adapter<RecorridosAdapter.ViewHolder> {

    private List<AllRecorridosUsuarioResponse.Recorrido> recorridos;
    private List<AllRecorridosUsuarioResponse.Recorrido> filteredRecorridos;

    public RecorridosAdapter(List<AllRecorridosUsuarioResponse.Recorrido> recorridos) {
        this.recorridos = new ArrayList<>(recorridos);
        this.filteredRecorridos = new ArrayList<>(recorridos);
    }

    // Filtro para buscar recorridos por el nombre de la bicicleta
    public void filter(String text) {
        filteredRecorridos.clear();
        if (text.isEmpty()) {
            filteredRecorridos.addAll(recorridos);
        } else {
            text = text.toLowerCase();
            for (AllRecorridosUsuarioResponse.Recorrido recorrido : recorridos) {
                if (recorrido.getBicicletaNombre().toLowerCase().contains(text)) {
                    filteredRecorridos.add(recorrido);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recorridos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filteredRecorridos.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredRecorridos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bicicletaNombre;
        private TextView calorias;
        private TextView tiempo;
        private TextView distanciaRecorrida;
        private TextView createdAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bicicletaNombre = itemView.findViewById(R.id.tv_bici);
            calorias = itemView.findViewById(R.id.tv_calorias);
            tiempo = itemView.findViewById(R.id.tv_tiempo);
            distanciaRecorrida = itemView.findViewById(R.id.tv_distancia);
            createdAt = itemView.findViewById(R.id.tv_fecha);
        }

        public void bind(AllRecorridosUsuarioResponse.Recorrido recorrido) {
            bicicletaNombre.setText(recorrido.getBicicletaNombre());
            calorias.setText(String.format("%.2f calorías", recorrido.getCalorias()));
            tiempo.setText(recorrido.getTiempo());
            distanciaRecorrida.setText(String.format("Recorrido %.2f km", recorrido.getDistanciaRecorrida()));
            createdAt.setText(recorrido.getCreatedAt());
        }
    }
}
