package com.example.itspower.request.userrequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateRequest {
    @NotBlank
    private String groupName;
    private String password;
    private int parentId;
    @JsonProperty("edit")
    private boolean isEdit;
    @JsonProperty("view")
    private boolean isView;
    @JsonProperty("report")
    private boolean isReport;
    @JsonProperty("admin")
    private boolean isAdmin;
}
