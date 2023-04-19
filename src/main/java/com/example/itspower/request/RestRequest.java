package com.example.itspower.request;

import lombok.Data;

@Data
public class RestRequest {
    private int restId;
    private int reasonId;
    private String restNameAndLabor;
    private String session;
    private Float workTime;
    private boolean isDelete = false;
}
