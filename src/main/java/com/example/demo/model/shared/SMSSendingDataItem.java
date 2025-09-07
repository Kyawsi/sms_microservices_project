package com.example.demo.model.shared;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSSendingDataItem {
    private Long id;
    @JsonProperty("create_at")
    private Long createAt;
    @JsonProperty("message_to")
    private String messageTo;
    @JsonProperty("message_text")
    private String messageText;
    private String operator;
    private String sender;
    @JsonProperty("is_delivered")
    private Boolean isDelivered;
    @JsonProperty("is_queuing")
    private Boolean isQueuing;
    private Boolean test;
    @JsonProperty("num_parts")
    private Integer numParts;
    private Integer credit;
}