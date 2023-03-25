package com.example.itspower.response.group;

import com.example.itspower.model.entity.GroupEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupRoleDemarcationRes {
    private Integer groupId;
    private String groupName;
    private Integer demarcationAvailable;

    public GroupRoleDemarcationRes(GroupEntity groupEntity) {
        this.groupId = groupEntity.getId();
        this.groupName = groupEntity.getGroupName();
        this.demarcationAvailable = groupEntity.getDemarcationAvailable();
    }
}
