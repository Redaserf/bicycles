package com.example.bicycles.Responses;

import com.example.bicycles.Models.Usuario;

public class LoginResponse {
    private String token;
    private String message;
    private Usuario user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
