package com.sam170703dev.megustapp.entidades;

import java.io.Serializable;

public class Valoracion implements Serializable {
    private int id;
    private int nota;
    private String comentario;

    private int clienteId;
    private int restauranteId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setRestauranteId(int restauranteId) {
        this.restauranteId = restauranteId;
    }

    public int getRestauranteId() {
        return restauranteId;
    }
}
