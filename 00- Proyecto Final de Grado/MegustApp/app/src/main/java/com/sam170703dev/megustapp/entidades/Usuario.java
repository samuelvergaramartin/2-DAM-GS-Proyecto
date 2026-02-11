package com.sam170703dev.megustapp.entidades;

public class Usuario {
    private int id;
    private String email;
    private String clave;

    public int getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public String getEmail() {
        return email;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }
}
