package com.sam170703dev.megustapp.entidades;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

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
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Ingrediente)) return false;

        if(this.nombre != null && ((Ingrediente) obj).nombre != null) {
            return this.nombre.equals(((Ingrediente) obj).nombre) && this.esAlergeno == ((Ingrediente) obj).esAlergeno;
        }
        else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, esAlergeno);
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
