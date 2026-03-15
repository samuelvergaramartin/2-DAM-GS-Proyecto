package com.sam170703dev.megustapp.entidades;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario implements Serializable {
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
        this.clave = encriptar(clave);
    }

    public void setClave(String clave, boolean encriptar) {
        if(encriptar) setClave(clave);
        else this.clave = clave;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String encriptar(String clave) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(clave.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xFF));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar SHA-256", e);
        }
    }
}
