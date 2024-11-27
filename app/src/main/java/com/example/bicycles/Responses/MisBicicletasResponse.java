package com.example.bicycles.Responses;

import com.example.bicycles.Models.Bicicleta;

import java.util.List;

public class MisBicicletasResponse {
    private String mensaje;
    private List<Bicicleta> bicicletas;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Bicicleta> getBicicletas() {
        return bicicletas;
    }

    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
    }
}
