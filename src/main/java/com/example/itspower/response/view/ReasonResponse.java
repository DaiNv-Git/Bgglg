package com.example.itspower.response.view;

import lombok.Data;

@Data
public class ReasonResponse {
    private Integer groupId;
    private Integer parentID;
    private String reasonName;
    private Double total;

    public ReasonResponse(Integer groupId, String reasonName, Double total) {
        this.groupId = groupId;
        this.reasonName = reasonName;
        this.total = total;
    }
    public ReasonResponse(Integer groupId, String reasonName, Double total,Integer parentId) {
        this.groupId = groupId;
        this.reasonName = reasonName;
        this.total = total;
        this.parentID=parentId;
    }
}
