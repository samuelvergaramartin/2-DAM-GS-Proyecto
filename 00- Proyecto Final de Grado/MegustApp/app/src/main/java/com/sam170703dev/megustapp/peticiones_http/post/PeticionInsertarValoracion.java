package com.sam170703dev.megustapp.peticiones_http.post;

import java.io.Serializable;

public class PeticionInsertarValoracion implements Serializable {
    private int nota;
    private String comentario;

    private int clienteId;
    private int restauranteId;

    public PeticionInsertarValoracion(int nota, String comentario, int clienteId, int restauranteId) {
        setNota(nota);
        setComentario(comentario);
        setClienteId(clienteId);
        setRestauranteId(restauranteId);
    }
    public void setNota(int nota) {
        this.nota = nota;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public void setRestauranteId(int restauranteId) {
        this.restauranteId = restauranteId;
    }
}
