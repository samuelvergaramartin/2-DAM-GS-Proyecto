package com.sam170703dev.megustapp.peticiones_http.post;

import java.io.Serializable;

public class PeticionTokenCliente implements Serializable {
    private final String client_id;
    private final String client_secret;
    private final String grant_type;

    public PeticionTokenCliente() {
        this.client_id = "jtGL5KU7bCjSXvvlO6Rb1Dq6sgoAGMpg";
        this.client_secret = "HBQt5FuFBq3nu58rt8nmQdnaEIitUdR8";
        this.grant_type = "client_credentials";
    }
}
