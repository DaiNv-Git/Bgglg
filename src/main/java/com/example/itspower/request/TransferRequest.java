package com.example.itspower.request;

import lombok.Data;

@Data
public class TransferRequest {
    private Integer groupId ;
     private Integer transferId;
    private Integer transferNum;
    private int type;
//    List<EmployeeInforResponse> employees;
}
