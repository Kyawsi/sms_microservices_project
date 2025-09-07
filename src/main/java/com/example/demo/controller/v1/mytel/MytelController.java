package com.example.demo.controller.v1.mytel;

import com.example.demo.model.request.mytel.MytelSendMessageRequest;
import com.example.demo.model.response.ResponseFormat;
import com.example.demo.service.v1.MytelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/mytel")
@RequiredArgsConstructor
@Slf4j
public class MytelController {

    private final MytelService mytelService;

    @PostMapping("/send")
    public ResponseEntity<ResponseFormat> send(@RequestBody MytelSendMessageRequest request) {
        ResponseFormat response = mytelService.getSingleSendMessage(request);
        return ResponseEntity.ok(response);
    }

}
