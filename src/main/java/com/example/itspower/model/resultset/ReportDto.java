package com.example.itspower.model.resultset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Integer id;
    private Integer groupId;
    private Integer demarcation;
    private Integer laborProductivity;
    private Integer transferNum;
    private Integer supportNum;
    private Double restNum;
    private Integer partTimeNum;
    private Integer studentNum;
    private Integer totalRice;
    private Date reportDate;
    private Integer professionNotLabor;
    private Integer professionLabor;

}
