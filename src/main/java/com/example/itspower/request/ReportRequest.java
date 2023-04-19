package com.example.itspower.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private int id;
    private int demarcation;
    private int restNum;
    private int laborProductivity;
    private int partTimeNum;
    private int studentNum;
    private int professionLabor;
    private int professionNotLabor;
    @Valid
    private RiceRequest riceRequests;
    @Valid
    private List<RestRequest> restRequests;
    @Valid
    private List<TransferRequest> transferRequests;
}
