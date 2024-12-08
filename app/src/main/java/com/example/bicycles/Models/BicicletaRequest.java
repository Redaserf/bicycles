package com.example.bicycles.Models;

import java.io.File;

public class BicicletaRequest {

    private String nombre;
    private File imagen;

    // Constructor
    public BicicletaRequest(String nombre, File imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public File getImagen() {
        return imagen;
    }

    public void setImagen(File imagen) {
        this.imagen = imagen;
    }

    public boolean hasImage(){
        if(getImagen() != null){
            return true;
        }

        return false;
    }
}
