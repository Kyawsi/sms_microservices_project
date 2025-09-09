package com.example.demo.controller.v1.twilio;

import com.example.demo.model.response.twilio.TwilioSmsResponse;
import com.example.demo.service.v1.TwilioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

import static com.example.demo.util.TwilioUtils.TWILIO_SENDER_ID;

@RestController
@RequestMapping("/v1/twilio")
@RequiredArgsConstructor
@Slf4j
public class TwilioController {

    private final TwilioService twilioService;

    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestParam String to, @RequestParam String body) {
        try {
            TwilioSmsResponse response = twilioService.sendSms(to, body);
            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            HttpStatus status = HttpStatus.valueOf(e.getRawStatusCode());
            try {
                Map<String, Object> errorJson = new ObjectMapper().readValue(e.getResponseBodyAsString(), Map.class);
                return ResponseEntity.status(status).body(errorJson);
            } catch (Exception ex) {
                Map<String, Object> fallback = Map.of(
                        "message", e.getResponseBodyAsString(),
                        "status", status.value()
                );
                return ResponseEntity.status(status).body(fallback);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "status", 500));
        }
    }




}
