package com.example.itspower.response.report;

import com.example.itspower.util.DateUtils;
import com.example.itspower.model.entity.RiceEntity;
import com.example.itspower.model.entity.TransferEntity;
import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.model.resultset.RestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReportResponse {
    private int id;
    private int demarcation;
    private int laborProductivity;
    private int transferNum;
    private int supportNum;
    private Double restNum;
    private int partTimeNum;
    private int studentNum;
    private int totalRice;
    private Integer professionNotLabor;
    private Integer professionLabor;
    private String reportDate;
    private int groupId;
    private RiceEntity rice;
    private List<RestDto> rests;
    private List<TransferEntity> transfers;

    public ReportResponse(ReportDto reportDto, RiceEntity rice, List<RestDto> rests, List<TransferEntity> transfers) {
        this.id = reportDto.getId();
        this.reportDate = DateUtils.formatDate(reportDto.getReportDate());
        this.demarcation = reportDto.getDemarcation();
        this.laborProductivity = reportDto.getLaborProductivity();
        this.transferNum = reportDto.getTransferNum();
        this.supportNum = reportDto.getSupportNum();
        this.restNum = reportDto.getRestNum();
        this.partTimeNum = reportDto.getPartTimeNum();
        this.studentNum = reportDto.getStudentNum();
        this.totalRice = reportDto.getTotalRice();
        this.groupId = reportDto.getGroupId();
        this.rice = rice;
        this.rests = rests;
        this.transfers = transfers;
        this.professionLabor = reportDto.getProfessionLabor();
        this.professionNotLabor = reportDto.getProfessionNotLabor();
    }

}
