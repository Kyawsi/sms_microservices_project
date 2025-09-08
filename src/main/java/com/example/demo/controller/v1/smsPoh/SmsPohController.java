package com.example.demo.controller.v1.smsPoh;


import com.example.demo.exception.SmsPohException;
import com.example.demo.model.request.sms.SMSSendingRequestV3;
import com.example.demo.model.response.ResponseFormat;
import com.example.demo.service.v1.SmsPohService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/smspoh")
@RequiredArgsConstructor
@Slf4j
public class SmsPohController {
    private final SmsPohService smsPohService;

    @PostMapping("/send")
    public ResponseEntity<ResponseFormat> sendSmsPoh(@RequestBody SMSSendingRequestV3 request) throws SmsPohException {
        ResponseFormat response = smsPohService.sendV3(request);
        return ResponseEntity.ok(response);
    }

}
