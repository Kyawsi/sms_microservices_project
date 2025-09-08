package com.example.demo.service.v1.impl.sms;

import com.example.demo.exception.SmsPohException;
import com.example.demo.model.request.sms.SMSSendingRequestV3;
import com.example.demo.model.response.ResponseFormat;
import com.example.demo.model.response.sms.SMSSendingResponse;
import com.example.demo.service.v1.SmsPohService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class SmsPohServiceImpl implements SmsPohService {

    private static final String SEND_SMS_URI_V3 = "/api/rest/send";

    @Value("${smspoh.base_url}") private String baseURL;
    @Value("${smspoh.sender}") private String sender;

    @Value("${smspoh.user_name}")
    private String smsUsername;

    @Value("${smspoh.password}")
    private String smsPassword;

    @Value("${smspoh.access_token}")
    private String accessToken;

    private WebClient webClient;


    @PostConstruct
    protected void initWebClientV3() {
        log.info("Initializing SMSPoh WebClient with base URL: {}", baseURL);
        this.webClient = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(headers -> {
                    headers.setBasicAuth(smsUsername, smsPassword);
                    log.info("Basic Auth header: {}", headers.getFirst(HttpHeaders.AUTHORIZATION));
                })
                .filter((request, next) -> {
                    log.info("Request Headers: {}", request.headers());
                    return next.exchange(request);
                })
                .build();
    }


    @Override
    public ResponseFormat sendV3(SMSSendingRequestV3 request) throws SmsPohException {
        try {
            request.setFrom(sender);
            log.info("Sending SMS via SMSPoh V3: {}", new ObjectMapper().writeValueAsString(request)); // log request
//            return accessSMSPohV3(request).doOnNext(res ->
//                    log.info("SMSPoh V3 Response: {}", res)
//            ).block();
            // Call SMSPoh but ignore upstream null response
            accessSMSPohV3(request)
                    .doOnNext(res -> {
                        try {
                            log.info("SMSPoh V3 Response: {}", new ObjectMapper().writeValueAsString(res));
                        } catch (Exception ex) {
                            log.error("Failed to log SMSPoh response", ex);
                        }
                    })
                    .block();

            ResponseFormat response = new ResponseFormat();
            response.setMessage(Optional.of("SMS sent successfully"));
            return response;
        } catch (WebClientResponseException e) {
            log.error("SMSPoh V3 Error Response: {}", e.getResponseBodyAsString());
            throw new SmsPohException(
                    e.getStatusCode(),
                    "Failed to send SMS via SMSPoh",
                    e.getMessage(),
                    e.getResponseBodyAsString()
            );
        } catch (Exception e) {
            log.error("Unexpected error in sendV3", e);
            throw new SmsPohException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", e.getMessage(), "");
        }
    }


    private Mono<SMSSendingResponse> accessSMSPohV3(SMSSendingRequestV3 request) {

//        String credentials = smsUsername + ":" + smsPassword;
//        String basicAuth = "Basic " + Base64.getEncoder()
//                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return webClient.post()
                .uri(SEND_SMS_URI_V3)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(SMSSendingResponse.class);
    }

}
