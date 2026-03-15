package com.sam170703dev.megustapp.peticiones_http.post;

import java.io.Serializable;

public class PeticionTokenRestaurante implements Serializable {
    private final String restaurant_id;
    private final String restaurant_secret;
    private final String grant_type;

    public PeticionTokenRestaurante() {
        this.restaurant_id = "kv1CuQmpwEZGbVrc1b2jltlE52ikyaXd";
        this.restaurant_secret = "PTjFpPjh7CRHvMPvkRsYUyzPwrpS3GUw";
        this.grant_type = "restaurant_credentials";
    }
}
