package com.example.demo.model.response.mytel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MessageResult {
    private MessageDataResult dataResult;
    private boolean isSuccessful;
    private String errorCode;
    private String errorMessage;
}