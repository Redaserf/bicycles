package com.example.bicycles.Responses;

import com.example.bicycles.Models.Bicicleta;

public class EditarBicicletaResponse {
    public String mensaje;
    public Bicicleta bicicleta;

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