package com.example.pruebaapi;

import java.io.Serializable;

public class Cuerpo implements Serializable {
    private String mensaje;

    public Cuerpo(String mensaje) {
        this.mensaje = mensaje;
    }
}
