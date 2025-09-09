package com.example.demo.service.v1.impl.mytel;

import com.example.demo.exception.SystemException;
import com.example.demo.model.request.mytel.MytelSendMessageRequest;
import com.example.demo.model.response.ResponseFormat;
import com.example.demo.model.response.mytel.*;
import com.example.demo.service.v1.MytelService;
import com.example.demo.util.HttpUtils;
import com.example.demo.util.MytelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MytelServiceImpl implements MytelService {

    @Autowired
    private MytelUtil mytelUtil;

    @Autowired
    private HttpUtils httpUtils;

//    @Override
//    public ResponseFormat getLoginToken() {
//        log.info("[getLoginToken] Calling MytelUtil...");
//
//        MytelTokenWrapper wrapper = mytelUtil.getMytelToken();
//
//        if (wrapper == null || wrapper.getResult() == null) {
//            throw new SystemException("Empty response from Mytel API");
//        }
//
//        MytelTokenResponse result = wrapper.getResult();
//
//        log.info("[getLoginToken] Mytel response result:");
//        log.info("  -> accessToken      = {}", result.getAccessToken());
//        log.info("  -> expireInSeconds  = {}", result.getExpireInSeconds());
//        log.info("  -> isError          = {}", result.isError());
//        log.info("  -> message          = {}", result.getMessage());
//
//        if (result.isError()) {
//            throw new SystemException("Mytel login failed: " + result.getMessage());
//        }
//
//        if (result.getAccessToken() == null || result.getAccessToken().isEmpty()) {
//            throw new SystemException("Access token is missing");
//        }
//
//        log.info("[getLoginToken] Token fetched successfully");
//
//        ResponseFormat responseFormat = new ResponseFormat();
//        responseFormat.setSuccess(true);
//        responseFormat.setMessage(Optional.of("Generate Token Successful"));
//        responseFormat.setData(Optional.of(result));
//        return responseFormat;
//    }


    @Override
    public ResponseFormat getSingleSendMessage(MytelSendMessageRequest request) {
        log.info("[getSingleSendMessage] Sending message to phone: {}", request.getPhoneNumbers());

        MessageResponse messageResponse = mytelUtil.sendMessage(request.getPhoneNumbers(), request.getContent());

        if (messageResponse == null || messageResponse.getResult() == null) {
            log.error("[getSingleSendMessage] Empty response from Mytel API");
            throw new SystemException("Empty response from Mytel API");
        }

        MessageResult result = messageResponse.getResult();

        if (!"ERR000".equals(result.getErrorCode())) {
            String errorMessage = result.getErrorMessage() != null ? result.getErrorMessage() : "Unknown error";
            log.error("[getSingleSendMessage] Mytel API error: {} (errorCode: {})", errorMessage, result.getErrorCode());
            throw new SystemException("Mytel API error: " + errorMessage);
        }

        MessageDataResult data = messageResponse.getResult().getDataResult();

        log.info("[getSingleSendMessage] Message sent. Campaign ID: {}, Message ID: {}",
                data != null ? data.getCampaignId() : null,
                data != null ? data.getMessageId() : null);

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setSuccess(true);
        responseFormat.setMessage(Optional.of("Message sent successfully"));
        responseFormat.setData(Optional.of(data));

        return responseFormat;
    }


}
