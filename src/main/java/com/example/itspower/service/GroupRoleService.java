package com.example.itspower.service;

import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.response.GroupRoleResponse;

import java.util.List;

public interface GroupRoleService {
    List<GroupRoleResponse> searchAll();
    List<GroupEntity> searchAllByParentId(int parentId);
    List<GroupEntity> searchAllByParentIdIsNull();
}
