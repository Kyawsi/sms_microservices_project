package com.example.demo.util;

import com.example.demo.exception.SystemException;
import com.example.demo.model.entity.OauthSmsConfig;
import com.example.demo.model.response.mytel.MessageResponse;
import com.example.demo.model.response.mytel.MytelTokenResponse;
import com.example.demo.model.response.mytel.MytelTokenWrapper;
import com.example.demo.repository.OauthSmsConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@Slf4j
@Component
public class MytelUtil {

    @Autowired
    private HttpUtils httpUtils;

    @Value("${mytel.baseurl}")
    public String mytel;

    @Value("${mytel_username}")
    public String username;
    @Value("${mytel_password}")
    public String password;
    @Value("${mytel_brandName}")
    public String brandName;

    @Autowired
    private OauthSmsConfigRepository configRepository;

    private static final String TOKEN_KEY = "mytel";

    /**
     * Request a new token directly from Mytel API
     */

    public MytelTokenWrapper requestNewTokenFromMytel() {
        String url = mytel + "/api/TokenAuth/AuthenticateAPI";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userNameOrEmailAddress", username);
        requestBody.put("password", password);

        ResponseEntity<MytelTokenWrapper> response = httpUtils.post(url, requestBody, headers, MytelTokenWrapper.class);

        return response.getBody();
    }


    /*** Retrieve token from DB or refresh if expired*/

    public String getMytelToken() {
        LocalDateTime now = LocalDateTime.now();

        OauthSmsConfig tokenConfig = configRepository.findByName(TOKEN_KEY)
                .orElse(new OauthSmsConfig());

        if (isValidToken(tokenConfig, now)) {
            return tokenConfig.getValue();
        }

        return refreshAndSaveToken(tokenConfig, now);
    }

    private boolean isValidToken(OauthSmsConfig config, LocalDateTime now) {
        return config.getValue() != null &&
                config.getExpireTime() != null &&
                config.getExpireTime().isAfter(now);
    }

    /** * Retrieve token from DB or refresh if expired */

    private String refreshAndSaveToken(OauthSmsConfig tokenConfig, LocalDateTime now) {
        MytelTokenWrapper wrapper = requestNewTokenFromMytel();

        if (wrapper == null || wrapper.getResult() == null || wrapper.getResult().isError()) {
            throw new SystemException("Failed to get valid Mytel access token");
        }

        MytelTokenResponse result = wrapper.getResult();
        String accessToken = result.getAccessToken();

        LocalDateTime expiryTime = now.plusSeconds(result.getExpireInSeconds() - 60);

        tokenConfig.setName(TOKEN_KEY);
        tokenConfig.setValue(accessToken);
        tokenConfig.setExpireTime(expiryTime);

        if (tokenConfig.getId() == null) {
            tokenConfig.setCreatedOn(now);
            tokenConfig.setGuid(UUID.randomUUID().toString());
            tokenConfig.setHidden(false);
        }

        tokenConfig.setUpdatedOn(now);

        configRepository.save(tokenConfig);

        return accessToken;
    }


    /**
     * Send SMS via Mytel
     */
    public MessageResponse sendMessage(List<String> phoneNumbers, String content) {
        try {

            String accessToken = getMytelToken();
            log.info("[sendMessage] Using access token: {}", accessToken);


            String url = mytel + "/api/services/app/MessageAPI/CreateMessage";
            log.info("[sendMessage] Sending message to Mytel API URL: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("brandName", brandName);
            requestBody.put("isSendNow", true);
            requestBody.put("scheduleDate", null);
            requestBody.put("content", content);
            requestBody.put("listPhoneNumber", phoneNumbers);

            log.info("[sendMessage] Request payload: {}", requestBody);

            ResponseEntity<MessageResponse> response = httpUtils.post(
                    url, requestBody, headers, MessageResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("[sendMessage] Message sent successfully. Response: {}", response.getBody());
                return response.getBody();
            } else {
                log.error("[sendMessage] HTTP error: {}, Response body: {}", response.getStatusCode(), response.getBody());
                throw new SystemException("Mytel returned HTTP error: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("[sendMessage] Unauthorized error from Mytel API: {}", e.getMessage(), e);
            throw new SystemException("Unauthorized - Invalid or expired Mytel access token");
        } catch (Exception e) {
            log.error("[sendMessage] Exception while sending message: {}", e.getMessage(), e);
            throw new SystemException("Failed to send message via Mytel", e);
        }
    }





}
