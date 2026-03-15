package com.sam170703dev.megustapp.respuestas_http;

import java.io.Serializable;

public class RespuestaToken implements Serializable {
    private String access_token;
    private long expires_in;
    private String token_type;

    public String getAccess_token() {
        return access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public String getToken_type() {
        return token_type;
    }
}
