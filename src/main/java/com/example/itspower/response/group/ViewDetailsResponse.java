package com.example.itspower.response.group;
import com.example.itspower.model.resultset.ViewAllDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class ViewDetailsResponse {
    private Integer groupId;
    private Integer groupParentId;
    private Float ratio;
    private String groupName;
    private Integer reportDemarcation;
    private Double laborProductivity;
    private Integer partTimeNum;
    private Double restNum;
    private Integer studentNum;
    private Integer riceCus;
    private Integer riceEmp;
    private Integer riceVip;
    private Float totalRatioOfOfficeAndDonvile;

    List<ViewDetailsResponse> children;

    public ViewDetailsResponse(ViewAllDto viewAllDto){
        this.groupId=viewAllDto.getGroupId();
        this.groupParentId=viewAllDto.getGroupParentId();
        this.groupName=viewAllDto.getGroupName();
        this.reportDemarcation=viewAllDto.getReportDemarcation();
        this.laborProductivity=viewAllDto.getLaborProductivity();
        this.partTimeNum=viewAllDto.getPartTimeNum();
        this.restNum=viewAllDto.getRestNum();
        this.studentNum=viewAllDto.getStudentNum();
        this.ratio=viewAllDto.getRatio()==null ?0.0f:viewAllDto.getRatio();
        this.riceCus=viewAllDto.getRiceCus();
        this.riceEmp=viewAllDto.getRiceEmp();
        this.riceVip=viewAllDto.getRiceVip();
    }
}
