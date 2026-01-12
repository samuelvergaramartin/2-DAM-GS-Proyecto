package com.example.pruebaapi;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + " {\n\tID: " + id + "\n\tEmail: " + email + "\n\tClave: " + clave + "\n}\n";
    }
}
