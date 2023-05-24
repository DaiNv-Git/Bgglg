package com.example.itspower.response.report;

import com.example.itspower.model.entity.RiceEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReportSearchResponse {
    private Integer id;
    private Integer groupId;
    private Integer demarcation;
    private Integer demarcationAvailable;
    private Float unproductiveLabor;
    private Double laborProductivity;
    private Integer numberStop;
    private Double restNum;
    private Integer partTimeNum;
    private Integer transferReceive;
    private Integer transferTo;
    private Integer studentNum;
    private Integer totalRice;
    private Date reportDate;
    private Integer professionNotLabor;
    private Integer professionLabor;
    private List<String> employeeStop;
    private List<String> employeeTransferTo;
    private List<String> employeeReceive;
    private List<String> restEmployee;

    private Integer riceCus;
    private Integer riceEmployee;
    private Integer riceVip;
    private Integer riceID;
    private RiceReportResponse  riceResponses;


    public ReportSearchResponse() {
        // Default constructor
    }

    public ReportSearchResponse(Integer id, Integer groupId, Integer demarcation, Integer demarcationAvailable,
                                Float unproductiveLabor, Double laborProductivity, Integer numberStop,
                                Double restNum, Integer partTimeNum, Integer transferReceive, Integer transferTo,
                                Integer studentNum, Integer totalRice, Date reportDate, Integer professionNotLabor,
                                Integer professionLabor,Integer riceCus, Integer riceEmployee,Integer riceVip,Integer riceID) {
        this.id = id;
        this.groupId = groupId;
        this.demarcation = demarcation;
        this.demarcationAvailable = demarcationAvailable;
        this.unproductiveLabor = unproductiveLabor;
        this.laborProductivity = laborProductivity;
        this.numberStop = numberStop;
        this.restNum = restNum;
        this.partTimeNum = partTimeNum;
        this.transferReceive = transferReceive;
        this.transferTo = transferTo;
        this.studentNum = studentNum;
        this.totalRice = totalRice;
        this.reportDate = reportDate;
        this.professionNotLabor = professionNotLabor;
        this.professionLabor = professionLabor;
        this.riceCus = riceCus;
        this.riceEmployee = riceEmployee;
        this.riceVip = riceVip;
        this.riceID=riceID;
    }

    // Getters and setters
}
