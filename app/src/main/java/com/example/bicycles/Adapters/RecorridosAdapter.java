package com.example.bicycles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicycles.R;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;

import java.util.List;

public class RecorridosAdapter extends RecyclerView.Adapter<RecorridosAdapter.ViewHolder> {

    private List<AllRecorridosUsuarioResponse.Recorrido> recorridos;

    // Constructor
    public RecorridosAdapter(List<AllRecorridosUsuarioResponse.Recorrido> recorridos) {
        this.recorridos = recorridos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recorridos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recorridos.get(position));
    }

    @Override
    public int getItemCount() {
        return recorridos.size();
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
            distanciaRecorrida.setText(String.format("Recorrido " + "%.2f km en", recorrido.getDistanciaRecorrida()));
            createdAt.setText(recorrido.getCreatedAt());
        }
    }
}
