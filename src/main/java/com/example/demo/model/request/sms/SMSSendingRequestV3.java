package com.example.demo.model.request.sms;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSSendingRequestV3 {
    private String to;
    private String message;
    private String from;
    private String clientReference;

}
