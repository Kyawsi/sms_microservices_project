package com.example.demo.service.v1;

import com.example.demo.model.request.mytel.MytelSendMessageRequest;
import com.example.demo.model.response.ResponseFormat;

public interface MytelService {

    ResponseFormat getSingleSendMessage(MytelSendMessageRequest request);
}
