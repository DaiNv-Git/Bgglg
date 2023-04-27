package com.example.itspower.response.view;

import lombok.Data;

@Data
public class ReasonResponse {
    private Integer groupId;
    private String reasonName;
    private Long total;

    public ReasonResponse(Integer groupId, String reasonName, Long total) {
        this.groupId = groupId;
        this.reasonName = reasonName;
        this.total = total;
    }
}
