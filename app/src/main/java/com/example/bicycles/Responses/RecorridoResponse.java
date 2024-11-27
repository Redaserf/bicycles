package com.example.bicycles.Responses;

import com.example.bicycles.Models.Recorrido;

public class RecorridoResponse {

    private String mensaje;
    private Recorrido recorrido;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Recorrido getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(Recorrido recorrido) {
        this.recorrido = recorrido;
    }
}
