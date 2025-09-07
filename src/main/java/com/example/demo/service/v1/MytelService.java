package com.example.demo.service.v1;

import com.example.demo.model.request.mytel.MytelSendMessageRequest;
import com.example.demo.model.response.ResponseFormat;

import java.util.List;

public interface MytelService {
    ResponseFormat getLoginToken();

    ResponseFormat getSingleSendMessage(MytelSendMessageRequest request);
}
