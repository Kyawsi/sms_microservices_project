package com.example.demo.model.response.mytel;

import lombok.Data;

@Data
public class MytelTokenResponse {
    private String accessToken;
    private int expireInSeconds;
    private boolean isError;
    private String message;
}
