package com.example.bicycles.Responses;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.Recorrido;;import java.util.List;

public class BicicletaResponse {

    private String mensaje;
    private Bicicleta bicicleta;

    private List<Recorrido> recorridos;

    public List<Recorrido> getRecorridos() {
        return recorridos;
    }

    public void setRecorridos(List<Recorrido> recorridos) {
        this.recorridos = recorridos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
    }
}
