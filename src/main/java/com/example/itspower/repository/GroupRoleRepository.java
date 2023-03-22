package com.example.itspower.repository;

import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.response.GroupRoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GroupRoleRepository {
    @Autowired
    private GroupJpaRepository groupJpaRepository;

    public GroupEntity save(String groupName, int parentId) {
        GroupEntity entity = new GroupEntity();
        entity.setGroupName(groupName);
        entity.setParentId(parentId);
        return groupJpaRepository.save(entity);
    }
    public void deleteGroupRole(int groupId){
        groupJpaRepository.deleteById(groupId);
    }

    public List<GroupRoleResponse> searchAll() {
        return getSubListChirdlen(groupJpaRepository.findAll());
    }

    public List<GroupEntity> findAllByParentId(int parentId) {
        return groupJpaRepository.findAllByParentId(parentId);
    }

    public List<GroupEntity> findAllByParentIdNotNull() {
        return groupJpaRepository.findAllByParentIdIsNull();
    }

    public List<GroupRoleResponse> getSubListChirdlen(List<GroupEntity> groups) {
        List<GroupRoleResponse> groupRoleResponses = new ArrayList<>();
        groups.forEach(i->{
            GroupRoleResponse groupRoleResponse = new GroupRoleResponse(i);
            groupRoleResponses.add(groupRoleResponse);
        });
        Map<Integer, List<GroupRoleResponse>> parentIdToChildren = groupRoleResponses.stream()
                .collect(Collectors.groupingBy(GroupRoleResponse::getParentId));
        groupRoleResponses.forEach(p -> p.setGroups(parentIdToChildren.get(p.getId())));

        return parentIdToChildren.get(0);
    }
}
