package com.example.bicycles.Models;

public class ReenviarRequest {
    private String email;

    public ReenviarRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
