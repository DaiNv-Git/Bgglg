package com.example.itspower.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RiceReportResponse {

    private Integer riceId;

    private Integer riceEmp = 0;

    private Integer riceCus = 0;

    private Integer riceVip = 0;
}
