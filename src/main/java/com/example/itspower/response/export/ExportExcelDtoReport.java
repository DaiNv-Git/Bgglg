package com.example.itspower.response.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelDtoReport {
    private String groupName;
    private Integer riceEmp;
    private Integer riceCus;
    private String reportDate;
}
