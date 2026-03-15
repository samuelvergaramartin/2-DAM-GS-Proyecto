package com.sam170703dev.megustapp.peticiones_http.post;

import java.io.Serializable;

public class PeticionCrearIngrediente implements Serializable {
    private String nombre;
    private boolean esAlergeno;

    public PeticionCrearIngrediente(String nombre, boolean esAlergeno) {
        setNombre(nombre);
        setEsAlergeno(esAlergeno);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsAlergeno() {
        return esAlergeno;
    }

    public void setEsAlergeno(boolean esAlergeno) {
        this.esAlergeno = esAlergeno;
    }
}
