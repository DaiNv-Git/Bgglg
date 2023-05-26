package com.example.itspower.response.group;

import com.example.itspower.model.entity.GroupEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupRoleDemarcationRes {
    private Integer groupId;
    private String groupName;
    private Float demarcationAvailable;

    public GroupRoleDemarcationRes(GroupEntity groupEntity,Float demarcation) {
        this.groupId = groupEntity.getId();
        this.groupName = groupEntity.getGroupName();
        this.demarcationAvailable = demarcation;
    }

    public GroupRoleDemarcationRes(Integer groupId, Float demarcationAvailable, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.demarcationAvailable = demarcationAvailable;
    }
}
