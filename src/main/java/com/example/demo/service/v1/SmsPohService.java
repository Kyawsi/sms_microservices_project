package com.example.demo.service.v1;


import com.example.demo.exception.SmsPohException;
import com.example.demo.model.request.sms.SMSSendingRequestV3;
import com.example.demo.model.response.sms.SMSSendingResponse;

public interface SmsPohService {

    SMSSendingResponse sendV3(SMSSendingRequestV3 request) throws SmsPohException;
}
