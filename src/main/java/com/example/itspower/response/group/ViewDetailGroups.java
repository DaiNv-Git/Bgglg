package com.example.itspower.response.group;

import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.response.view.RiceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewDetailGroups {
    private Integer key;
    private int parentId;
    private String name;
    private Float office;
    private Float enterprise;
    private Float laborProductivity;
    private Float ratio;
    private Float totalLaborProductivity;
    private Integer numberLeave;
    private Integer partTimeEmp;
    private Float totalRatioOfOfficeAndDonvile;

    private RiceResponse rice;
    List<ViewDetailGroups> children;


    public ViewDetailGroups(ViewAllDto response) {
        this.key = response.getGroupId();
        this.name = response.getGroupName();
        if(name.equalsIgnoreCase("văn phòng")){
            this.office = Float.valueOf(response.getReportDemarcation());
            this.totalRatioOfOfficeAndDonvile=response.getTotalRatioOfOfficeAndDonvile();
            this.totalLaborProductivity=response.getTotalLaborProductivity();
        }else{
            this.enterprise = Float.valueOf(response.getReportDemarcation());
        }
        this.laborProductivity = Float.valueOf(response.getLaborProductivity());
        this.numberLeave = response.getRestNum();
        this.ratio = response.getRatio();
        this.rice = new RiceResponse(response.getRiceCus(), response.getRiceVip(), response.getRiceEmp());
    }
}