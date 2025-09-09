package com.example.demo.service.v1.impl.twilio;

import com.example.demo.model.response.twilio.TwilioSmsResponse;
import com.example.demo.service.v1.TwilioService;
import com.example.demo.util.TwilioUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.example.demo.util.TwilioUtils.TWILIO_SENDER_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwilioServiceImpl implements TwilioService {

    private final WebClient.Builder webClientBuilder;

    @Value("${twilio.api.username}")
    private String username;

    @Value("${twilio.api.password}")
    private String password;

    @Override
    public TwilioSmsResponse sendSms(String to, String body) {
        try {
            String url = TwilioUtils.buildSendSmsUrl(username);
            String basicAuth = TwilioUtils.buildBasicAuth(username, password);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("To", to);
            formData.add("From", TWILIO_SENDER_ID);
            formData.add("Body", "WeDay : " + body);
            log.info("Sending SMS to {} via Twilio...", to);

            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, basicAuth)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(TwilioSmsResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Twilio API error: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected Twilio error", e);
            throw e;
        }
    }
}