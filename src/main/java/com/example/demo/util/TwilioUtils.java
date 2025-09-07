package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public final class TwilioUtils {

    private static final String BASE_URL = "https://api.twilio.com/2010-04-01/Accounts";
    private static final String MESSAGES_PATH = "Messages.json";
    public static final String TWILIO_SENDER_ID = "WeDay";

    private TwilioUtils() {
    }

    public static String buildSendSmsUrl(String accountSid) {
        String url = String.format("%s/%s/%s", BASE_URL, accountSid, MESSAGES_PATH);
        log.debug("Twilio SMS URL built: {}", url);
        return url;
    }

    public static String buildBasicAuth(String accountSid, String authToken) {
        String credentials = accountSid + ":" + authToken;
        return "Basic " + Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
