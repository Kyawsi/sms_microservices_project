package com.example.demo.model.request.mytel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MytelSendMessageRequest {
    private List<String> phoneNumbers;
    private String content;
}
