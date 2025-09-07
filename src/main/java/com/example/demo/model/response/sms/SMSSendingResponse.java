package com.example.demo.model.response.sms;


import com.example.demo.model.shared.SMSSendingData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSSendingResponse {
    private Boolean status;
    private SMSSendingData data;
}
