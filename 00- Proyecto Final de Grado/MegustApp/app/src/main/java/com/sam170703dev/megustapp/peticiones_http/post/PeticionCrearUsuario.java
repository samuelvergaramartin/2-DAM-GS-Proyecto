package com.sam170703dev.megustapp.peticiones_http.post;

import java.io.Serializable;

public class PeticionCrearUsuario implements Serializable {
    private String email;
    private String clave;

    public PeticionCrearUsuario(String email, String clave) {
        setClave(clave);
        setEmail(email);
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
}
