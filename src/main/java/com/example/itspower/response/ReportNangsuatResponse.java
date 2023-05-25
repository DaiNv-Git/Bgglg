package com.example.itspower.response;

import lombok.Data;

@Data
public class ReportNangsuatResponse {
    private Integer id;
    private String name;
    private Double demarcation;
    private Integer restNum;
    private Double laborProductivity;
    private Integer stopNumber;
    private Integer newNumber;

    public ReportNangsuatResponse(String name, Double demarcation, Integer restNum, Double laborProductivity, Integer stopNumber, Integer newNumber) {
        this.name = name;
        this.demarcation = demarcation;
        this.restNum = restNum;
        this.laborProductivity = laborProductivity;
        this.stopNumber = stopNumber;
        this.newNumber = newNumber;
    }
}
