package com.example.bicycles.Responses;

import java.util.List;

public class AllRecorridosUsuarioResponse {
    private String message;
    private List<Recorrido> recorridos;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Recorrido> getRecorridos() {
        return recorridos;
    }

    public void setRecorridos(List<Recorrido> recorridos) {
        this.recorridos = recorridos;
    }

    public static class Recorrido {
        private String bicicleta_nombre;
        private double calorias;
        private String tiempo;
        private double velocidad_promedio;
        private double velocidad_maxima;
        private double distancia_recorrida;
        private String created_at;

        public String getBicicletaNombre() {
            return bicicleta_nombre;
        }

        public void setBicicletaNombre(String bicicleta_nombre) {
            this.bicicleta_nombre = bicicleta_nombre;
        }

        public double getCalorias() {
            return calorias;
        }

        public void setCalorias(double calorias) {
            this.calorias = calorias;
        }

        public String getTiempo() {
            return tiempo;
        }

        public void setTiempo(String tiempo) {
            this.tiempo = tiempo;
        }

        public double getVelocidadPromedio() {
            return velocidad_promedio;
        }

        public void setVelocidadPromedio(double velocidad_promedio) {
            this.velocidad_promedio = velocidad_promedio;
        }

        public double getVelocidadMaxima() {
            return velocidad_maxima;
        }

        public void setVelocidadMaxima(double velocidad_maxima) {
            this.velocidad_maxima = velocidad_maxima;
        }

        public double getDistanciaRecorrida() {
            return distancia_recorrida;
        }

        public void setDistanciaRecorrida(double distancia_recorrida) {
            this.distancia_recorrida = distancia_recorrida;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public void setCreatedAt(String created_at) {
            this.created_at = created_at;
        }
    }
}
