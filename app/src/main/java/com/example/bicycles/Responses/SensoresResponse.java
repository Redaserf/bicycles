package com.example.bicycles.Responses;

public class SensoresResponse {
    private boolean success;
    private String message;
    private double velocidad_actual;
    private double velocidad_maxima;
    private double velocidad_promedio;
    private double distancia_recorrida;
    private double calorias;
    private double temperatura;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getVelocidadActual() {
        return velocidad_actual;
    }

    public void setVelocidadActual(double velocidad_actual) {
        this.velocidad_actual = velocidad_actual;
    }

    public double getVelocidadMaxima() {
        return velocidad_maxima;
    }

    public void setVelocidadMaxima(double velocidad_maxima) {
        this.velocidad_maxima = velocidad_maxima;
    }

    public double getVelocidadPromedio() {
        return velocidad_promedio;
    }

    public void setVelocidadPromedio(double velocidad_promedio) {
        this.velocidad_promedio = velocidad_promedio;
    }

    public double getDistanciaRecorrida() {
        return distancia_recorrida;
    }

    public void setDistanciaRecorrida(double distancia_recorrida) {
        this.distancia_recorrida = distancia_recorrida;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
}
