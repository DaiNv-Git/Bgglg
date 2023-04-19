package com.example.itspower.model.resultset;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private int userId;
    private String groupName;
    private boolean isAdmin;
    private boolean isEdit;
    private boolean isReport;
    private boolean isView;
    private Integer groupId;

    public UserDto(int userId, boolean isAdmin, String groupName, boolean isEdit, boolean isReport, boolean isView, Integer groupId) {
        this.userId = userId;
        this.groupName = groupName;
        this.isAdmin = isAdmin;
        this.isEdit = isEdit;
        this.isReport = isReport;
        this.isView = isView;
        this.groupId = groupId;
    }
}
