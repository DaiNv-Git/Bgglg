package com.example.itspower.response.view;

import lombok.Data;

import java.util.List;

@Data
public class RestObjectResponse {
    private Double restNum;
    private List<ReasonRest> reason;
    private List<reasonEmployee> employeeRest;

    public RestObjectResponse(Double restNum, List<ReasonRest> reason) {
        this.restNum = restNum;
        this.reason = reason;
    }

    public RestObjectResponse(Double restNum, List<ReasonRest> reason, List<reasonEmployee> employeeRest) {
        this.restNum = restNum;
        this.reason = reason;
        this.employeeRest = employeeRest;
    }
}

class reasonEmployee {
    private Integer employeeRest;
    private String labor;
    private String reason;
}