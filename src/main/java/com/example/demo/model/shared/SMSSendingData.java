package com.example.demo.model.shared;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SMSSendingData {
    private List<SMSSendingDataItem> messages;
    private Double balance;
    private Integer credit;
}
