package com.example.itspower.request;

import com.example.itspower.response.employee.EmployeeInforResponse;
import lombok.Data;

import java.util.List;

@Data
public class TransferRequest {
    private Integer groupId ;
     private Integer transferId;
    private Integer transferNum;
    private int type;
    List<EmployeeInforResponse> employees;
}
