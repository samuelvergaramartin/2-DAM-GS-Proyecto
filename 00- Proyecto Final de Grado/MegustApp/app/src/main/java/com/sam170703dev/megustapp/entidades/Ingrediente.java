package com.sam170703dev.megustapp.entidades;

import java.io.Serializable;

public class Ingrediente implements Serializable {
    private int id;
    private String nombre;
    private boolean esAlergeno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Ingrediente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", esAlergeno=" + esAlergeno +
                '}';
    }
}
