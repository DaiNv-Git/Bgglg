package com.example.itspower.request;

import lombok.Data;

@Data
public class RestRequest {
    private int restId;
    private int reasonId;
    private String restName;
    private String restEmployeeLabor;
    private String session;
    private Float workTime;
    private boolean isDelete = false;
}
