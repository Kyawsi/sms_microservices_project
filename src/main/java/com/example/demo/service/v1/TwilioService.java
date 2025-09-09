package com.example.demo.service.v1;

import com.example.demo.model.response.twilio.TwilioSmsResponse;

public interface TwilioService {
    TwilioSmsResponse sendSms(String to, String body);
}
