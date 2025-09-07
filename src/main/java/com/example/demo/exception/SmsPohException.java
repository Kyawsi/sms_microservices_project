package com.example.demo.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SmsPohException extends RuntimeException {

    private HttpStatus statusCode;
    private String message;
    private String rawMessage;
    private String rawBody;

    public SmsPohException(String message) {
        super(message);
        this.message = message;
    }

    public SmsPohException(HttpStatus statusCode, String message, String rawMessage, String rawBody) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.rawMessage = rawMessage;
        this.rawBody = rawBody;
    }
}
