package com.example.itspower.service.impl;

import com.example.itspower.model.entity.EmployeeGroupEntity;
import com.example.itspower.repository.repositoryjpa.EmployeeGroupRepository;
import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.employee.EmployeeGroupResponse;
import com.example.itspower.service.EmployeeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeGroupServiceImpl implements EmployeeGroupService {
    @Autowired
    EmployeeGroupRepository groupRepository;

    @Override
    public void saveAll(List<addUserRequest> addUser) {
        List<EmployeeGroupEntity> save = new ArrayList<>();
        for (addUserRequest addUserRequest : addUser) {
            EmployeeGroupEntity employeeGroup = new EmployeeGroupEntity();
            if (addUserRequest.getId() != null) {
                employeeGroup.setId(addUserRequest.getId());
            }
            employeeGroup.setGroupId(addUserRequest.getGroupId());
            employeeGroup.setLaborCode(addUserRequest.getLaborCode());
            employeeGroup.setName(addUserRequest.getName());
            save.add(employeeGroup);
        }
        groupRepository.saveAll(save);
    }


    @Override
    public void delete(List<Integer> ids) {
        groupRepository.deleteAllById(ids);
    }

    @Override
    public PageResponse getEmployee(String groupName, Integer groupId, String laborCode, String employeeName, int pageSize, int pageNo) {
        int offset = pageSize * (pageNo - 1);
        int countEmployee = groupRepository.countEmployee();
        List<EmployeeGroupResponse> res = groupRepository.getEmployee(groupId, groupName, laborCode, employeeName, pageSize, offset);
        Pageable pageable = PageRequest.of(offset, pageSize);
        final Page<EmployeeGroupResponse> page = new PageImpl<>(res, pageable, 0);
        return new PageResponse<>(page, (long) countEmployee);
    }
}
