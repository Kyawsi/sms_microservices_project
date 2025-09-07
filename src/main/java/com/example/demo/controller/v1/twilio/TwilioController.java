package com.example.demo.controller.v1.twilio;

import com.example.demo.model.response.twilio.TwilioSmsResponse;
import com.example.demo.service.v1.TwilioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/twilio")
@RequiredArgsConstructor
@Slf4j
public class TwilioController {

    private final TwilioService twilioService;

    @PostMapping("/send")
    public ResponseEntity<TwilioSmsResponse> sendSms(
            @RequestParam String to,
            @RequestParam String from,
            @RequestParam String body
    ) {
        try {
            TwilioSmsResponse response = twilioService.sendSms(to, from, body);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending SMS", e);
            return ResponseEntity.status(500).build();
        }
    }
}
