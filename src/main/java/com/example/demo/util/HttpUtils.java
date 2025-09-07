package com.example.demo.util;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class HttpUtils {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType) {
        return callApi(url, HttpMethod.GET, null, headers, responseType);
    }

    public <T> ResponseEntity<T> post(String url, Object body, HttpHeaders headers, Class<T> responseType) {
        return callApi(url, HttpMethod.POST, body, headers, responseType);
    }

    public <T> ResponseEntity<T> put(String url, Object body, HttpHeaders headers, Class<T> responseType) {
        return callApi(url, HttpMethod.PUT, body, headers, responseType);
    }

    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType) {
        return callApi(url, HttpMethod.DELETE, null, headers, responseType);
    }

    private <T> ResponseEntity<T> callApi(String url, HttpMethod method, Object body, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }
//    public  <T> ResponseEntity<T> callApiV2(String url, HttpMethod method, HttpEntity<Object> objectHttpEntitys, ParameterizedTypeReference<T> responseType) {
//        return restTemplate.exchange(url, method, objectHttpEntitys, responseType);
//    }

    public HttpHeaders buildAuthHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return headers;
    }



}

