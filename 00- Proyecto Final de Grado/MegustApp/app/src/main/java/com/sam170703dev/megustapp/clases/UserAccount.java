package com.sam170703dev.megustapp.clases;

import com.sam170703dev.megustapp.enums.UserAccountTypes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class UserAccount {
    private int id;
    private String usuario;
    private String clave;
    private final UserAccountTypes TIPO;

    public UserAccount(int id, String usuario, String clave, UserAccountTypes tipo) {
        setId(id);
        setUsuario(usuario);
        setClave(clave);
        TIPO = tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getClave() {
        return clave;
    }

    public UserAccountTypes getTIPO() {
        return TIPO;
    }

    public int getId() {
        return id;
    }

    public boolean esClaveCorrecta(String clave) {
        return encriptar(clave).equals(this.clave);
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
