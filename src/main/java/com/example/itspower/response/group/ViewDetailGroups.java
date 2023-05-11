package com.example.itspower.response.group;

import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.response.view.RestObjectResponse;
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
    private RestObjectResponse restObjectResponse;
    private Boolean isOpen=false;
    List<ViewDetailGroups> children;


    public ViewDetailGroups(ViewAllDto response,Integer officeId) {
        this.key = response.getGroupId();
        this.parentId =  response.getGroupParentId();
        this.name = response.getGroupName();
        if(name.equalsIgnoreCase("văn phòng")||parentId==officeId && response.getGroupId() >0 ) {
             this.office = Float.valueOf(response.getReportDemarcation());
             this.totalRatioOfOfficeAndDonvile=response.getTotalRatioOfOfficeAndDonvile();
             this.totalLaborProductivity=response.getTotalLaborProductivity();
        }else if (name.equalsIgnoreCase("Đơn vị lẻ")){
            this.totalRatioOfOfficeAndDonvile=response.getTotalRatioOfOfficeAndDonvile();
        }
        else{
            this.enterprise = Float.valueOf(response.getReportDemarcation());
        }
        this.laborProductivity = Float.valueOf(response.getLaborProductivity());
        this.numberLeave = response.getRestNum();
        this.ratio = response.getRatio();
        this.rice = new RiceResponse(response.getRiceCus(), response.getRiceVip(), response.getRiceEmp());
        this.restObjectResponse=response.getRestObjectResponse();
    }
}