package com.credable.email.Response;

import lombok.Data;

@Data
public class Response {
    private int statusCode;
    private String message;
    private Object object;

    public Response(int statusCode, String message, Object object) {
        this.statusCode = statusCode;
        this.message = message;
        this.object = object;
    }

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

