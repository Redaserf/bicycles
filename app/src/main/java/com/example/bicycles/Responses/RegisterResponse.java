package com.example.bicycles.Responses;

import com.example.bicycles.Models.Usuario;

public class RegisterResponse {
    private String mensaje;
    private Usuario usuario;

    public Usuario getUser() {
        return usuario;
    }

    public void setUser(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
