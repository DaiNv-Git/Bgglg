package com.example.itspower.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private int id;
    private Float demarcation;
    private Float demarcationAvailable;
    private Float restNum;
    private Float laborProductivity;
    private Float partTimeNum;
    private int studentNum;
    private int professionLabor;
    private int professionNotLabor;
    private RiceRequest riceRequests;
    private List<RestRequest> restRequests;
    private List<TransferRequest> transferRequests;
}
