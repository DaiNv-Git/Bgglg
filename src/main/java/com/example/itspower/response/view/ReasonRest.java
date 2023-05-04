package com.example.itspower.response.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReasonRest {
    private int groupId;
    private int parentId;
    private String reasonName;
    private int total;

    public ReasonRest( int total,String reasonName) {
        this.reasonName = reasonName;
        this.total = total;
    }
}
