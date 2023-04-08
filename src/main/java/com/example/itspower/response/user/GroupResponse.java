package com.example.itspower.response.user;

import com.example.itspower.model.entity.GroupEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupResponse {
    private String groupName = "";
    private Integer parentId;
    private Integer demarcationAvailable;

    public GroupResponse(GroupEntity groupEntity) {
        this.groupName = groupEntity.getGroupName();
        this.parentId = groupEntity.getParentId();
        this.demarcationAvailable = groupEntity.getDemarcationAvailable();
    }
}
