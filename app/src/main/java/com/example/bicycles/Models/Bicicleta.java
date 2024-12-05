package com.example.bicycles.Models;

import java.util.Date;

public class Bicicleta {

    private int id;
    private String nombre;
    private int usuario_id;
    private Date created_at;
    private Date updated_at;

    public Bicicleta(String nombre, int usuario_id) {
        this.nombre = nombre;
        this.usuario_id = usuario_id;
    }

    public Bicicleta(String nombre){//para crear una bici
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
