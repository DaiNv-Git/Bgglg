package com.example.itspower.request;

import lombok.Data;

import java.util.List;

@Data
public class TransferRequest {
    private Integer groupId ;
     private Integer transferId;
    private Integer transferNum;
    List<String> employees;
}
