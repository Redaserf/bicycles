package com.example.bicycles.Models;

import java.lang.ref.SoftReference;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private Double peso;
    private String email;
    private String password;

    public Usuario(String nombre, String apellido, Double peso, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.peso = peso;
        this.email = email;
        this.password = password;
    }

    public Usuario(String nombre, String apellido, Double peso, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.peso = peso;
        this.email = email;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
