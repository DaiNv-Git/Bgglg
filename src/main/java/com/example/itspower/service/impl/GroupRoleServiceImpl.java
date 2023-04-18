package com.example.itspower.service.impl;

import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.resultset.GroupRoleDto;
import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.repository.GroupRoleRepository;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.request.GroupRoleRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.group.GroupRoleDemarcationRes;
import com.example.itspower.response.group.GroupRoleResponse;
import com.example.itspower.response.group.ResponseCount;
import com.example.itspower.service.GroupRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupRoleServiceImpl implements GroupRoleService {
    @Autowired
    private GroupRoleRepository groupRoleRepository;
    @Autowired
    private GroupJpaRepository groupJpaRepository;

    private static final String OFFICE = "Office";
    private static final String VANPHONG = "Văn phòng";
    private static final String DONVILE = "Đơn vị lẻ";
    private static final String HOC_SINH_CHUA_BAO_NANG_SUAT = "Học sinh chưa báo năng suất";
    private static final String THOI_VU_TO_MAY = "Thời vụ tổ may";

    @Override
    public List<GroupRoleResponse> searchAll() {
        return getSubListChildren(groupRoleRepository.searchAll());
    }

    @Override
    public PageResponse getAllDamercation(String groupName, int pageSize, int pageNo) {
        try {
            int offset = (pageNo - 1) * pageSize;
            int countGroupRole = groupJpaRepository.countGroupRole();
            List<GroupEntity> findAll = groupJpaRepository.findByGroupName(groupName, pageSize, offset);
            Pageable pageable = PageRequest.of(offset, pageSize);
            final PageImpl<GroupEntity> page = new PageImpl<>(findAll, pageable, 0);
            return new PageResponse<>(page, (long) countGroupRole);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<Integer> searchAllDeleteTM() {
        List<GroupRoleResponse> a = getSubListChildren(groupRoleRepository.searchAll());
        removeGroupRoleById(a);
        List<Integer> labelList = new ArrayList<>();
        for (GroupRoleResponse groupRole : a) {
            labelList.add(groupRole.getValue());
            if (groupRole.getChildren() != null) {
                for (GroupRoleResponse childGroupRole : groupRole.getChildren()) {
                    labelList.add(childGroupRole.getValue());
                    if (childGroupRole.getChildren() != null) {
                        for (GroupRoleResponse grandChildGroupRole : childGroupRole.getChildren()) {
                            labelList.add(grandChildGroupRole.getValue());
                        }
                    }
                }
            }
        }
        return labelList;
    }

    public List<ResponseCount> count(String reportDate) {
        List<GroupRoleResponse> a = getDetailsReport(reportDate);
        removeGroupRoleById(a);
        List<ResponseCount> labelList = new ArrayList<>();
        for (GroupRoleResponse item : a) {
            int parentId = item.getParentId();
            if (parentId == 0) {
                int count = 0;
                for (GroupRoleResponse child : item.getChildren()) {
                    if (child.getParentId() == item.getValue()) {
                        count++;
                    }
                    int countChild = 0;
                    if (child.getChildren() != null) {
                        for (GroupRoleResponse childrent : child.getChildren()) {
                            if (childrent.getParentId() == child.getValue()) {
                                countChild++;
                            }
                            int countChildParent = 0;
                            if (childrent.getChildren() != null) {
                                for (GroupRoleResponse childrents : childrent.getChildren()) {
                                    if (childrents.getParentId() == childrent.getValue()) {
                                        countChildParent++;
                                    }
                                }
                                labelList.add(new ResponseCount(childrent.getValue(), childrent.getLabel(), countChildParent));
                            }
                        }
                        labelList.add(new ResponseCount(child.getValue(), child.getLabel(), countChild));
                    }
                }
                labelList.add(new ResponseCount(item.getValue(), item.getLabel(), count));
            }
        }
        return labelList;
    }

    public void removeGroupRoleById(List<GroupRoleResponse> list) {
        String groupName = "Tổ may";
        Optional<GroupEntity> optionalGroupEntity = groupRoleRepository.findByGroupName(groupName);
        for (GroupRoleResponse groupRole : list) {
            if (groupRole.getValue() == optionalGroupEntity.get().getId()) {
                list.remove(groupRole);
                return;
            }
            if (groupRole.getChildren() != null) {
                removeGroupRoleById(groupRole.getChildren());
            }
        }
    }

    public List<GroupRoleResponse> getSubListChildren(List<GroupRoleDto> groups) {
        List<GroupRoleResponse> groupRoleResponses = new ArrayList<>();
        groups.forEach(i -> {
            GroupRoleResponse groupRoleResponse = new GroupRoleResponse(i);
            groupRoleResponses.add(groupRoleResponse);
        });
        Map<Integer, List<GroupRoleResponse>> parentIdToChildren = groupRoleResponses.stream().collect(Collectors.groupingBy(GroupRoleResponse::getParentId));
        groupRoleResponses.forEach(p -> p.setChildren(parentIdToChildren.get(p.getValue())));
        return parentIdToChildren.get(0);
    }

    private List<GroupRoleResponse> getDetailsReport(String reportDate) {
        List<ViewAllDto> mapReport = groupRoleRepository.searchAllView(reportDate);
        List<GroupRoleResponse> mapData = new ArrayList<>();
        for (ViewAllDto mapChildren : mapReport) {
            mapData.add(new GroupRoleResponse(mapChildren));
        }
        Map<Integer, List<GroupRoleResponse>> parentIdToChildren =
                mapData.stream().collect(Collectors.groupingBy(GroupRoleResponse::getParentId));
        mapData.forEach(p -> p.setChildren(parentIdToChildren.get(p.getValue())));
        return parentIdToChildren.get(0);
    }


    @Override
    public List<GroupEntity> searchAllByParentId(int parentId) {
        return groupRoleRepository.findAllByParentId(parentId);
    }

    @Override
    public List<GroupEntity> searchAllByParentIdIsNull() {
        return groupRoleRepository.findAllByParentIdNotNull();
    }

    @Override
    public List<String> getName() {
        return groupRoleRepository.getName();
    }

    @Override
    public Object getDemarcationRes(Integer groupId) {
        return groupRoleRepository.getDemarcationRes(groupId);
    }

    @Override
    public List<GroupRoleDemarcationRes> getAllDemarcationRes() {
        return null;
    }

    @Override
    public Object updateGroupRole(Integer id, Integer demarcation) {

        Optional<GroupEntity> group = groupRoleRepository.findById(id);
        if (group.isEmpty()) {
            return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "parentName sai ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        GroupEntity groupEntity = groupRoleRepository.update(group.get().getId(), group.get().getGroupName(), group.get().getParentId(), demarcation);
        return new SuccessResponse<>(HttpStatus.OK.value(), "update group demarcation success", groupEntity);
    }

    @Override
    public void delete(Integer groupId) {
        try {
            groupRoleRepository.delete(groupId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object save(GroupRoleRequest groupRoleRequest) {
        Optional<GroupEntity> groupRoleCheck = groupRoleRepository.findByGroupNameAndParentId(groupRoleRequest.getGroupName(), groupRoleRequest.getParentId());
        if (groupRoleCheck.isPresent()) {
            return new SuccessResponse<>(HttpStatus.BAD_REQUEST.value(), "Parent Id is not exits", null);
        }
        return new SuccessResponse<>(HttpStatus.OK.value(), "Save group success!",
                groupRoleRepository.save(groupRoleRequest, groupRoleRequest.getParentId()));
    }

    public Object getViewRoot() {
        return groupRoleRepository.getViewRoot();
    }
}
