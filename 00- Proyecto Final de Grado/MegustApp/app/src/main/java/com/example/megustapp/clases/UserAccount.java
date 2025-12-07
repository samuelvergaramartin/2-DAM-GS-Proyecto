package com.example.megustapp.clases;

import com.example.megustapp.enums.UserAccountTypes;

public abstract class UserAccount {
    private String usuario;
    private String clave;
    private final UserAccountTypes TIPO;

    public UserAccount(String usuario, String clave, UserAccountTypes tipo) {
        this.usuario = usuario;
        this.clave = clave;
        TIPO = tipo;
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
}
