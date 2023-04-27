package com.example.itspower.response.view;

import lombok.Data;

@Data
public class ListNameRestResponse {
    private Integer groupId;
    private String nameEmployee;
    private String labor;
    private String reasonName;

    public ListNameRestResponse(Integer groupId, String nameEmployee, String reasonName,String labor) {
        this.groupId = groupId;
        this.nameEmployee = nameEmployee;
        this.reasonName = reasonName;
        this.labor = labor;
    }
}
