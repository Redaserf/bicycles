package com.example.bicycles.Models;

import java.time.LocalTime;

public class Recorrido {

    private int id;
    private int usuario_id;
    private int bicicleta_id;
    private double calorias;
    private LocalTime tiempo;
    private double velocidad_promedio;
    private double velocidad_maxima;
    private double distancia_recorrida;

    public Recorrido(int usuario_id, int bicicleta_id, LocalTime tiempo) {
        this.usuario_id = usuario_id;
        this.bicicleta_id = bicicleta_id;
        this.tiempo = tiempo;
        this.calorias = 0.0;
        this.velocidad_promedio = 0.0;
        this.velocidad_maxima = 0.0;
        this.distancia_recorrida = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getBicicleta_id() {
        return bicicleta_id;
    }

    public void setBicicleta_id(int bicicleta_id) {
        this.bicicleta_id = bicicleta_id;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public LocalTime getTiempo() {
        return tiempo;
    }

    public void setTiempo(LocalTime tiempo) {
        this.tiempo = tiempo;
    }

    public double getVelocidad_promedio() {
        return velocidad_promedio;
    }

    public void setVelocidad_promedio(double velocidad_promedio) {
        this.velocidad_promedio = velocidad_promedio;
    }

    public double getVelocidad_maxima() {
        return velocidad_maxima;
    }

    public void setVelocidad_maxima(double velocidad_maxima) {
        this.velocidad_maxima = velocidad_maxima;
    }

    public double getDistancia_recorrida() {
        return distancia_recorrida;
    }

    public void setDistancia_recorrida(double distancia_recorrida) {
        this.distancia_recorrida = distancia_recorrida;
    }
}
