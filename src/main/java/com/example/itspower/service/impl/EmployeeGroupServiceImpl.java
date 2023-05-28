package com.example.itspower.service.impl;

import com.example.itspower.model.entity.EmployeeGroupEntity;
import com.example.itspower.repository.repositoryjpa.EmployeeGroupRepository;
import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.employee.EmployeeExportExcel;
import com.example.itspower.response.employee.EmployeeGroupResponse;
import com.example.itspower.service.EmployeeGroupService;
import com.example.itspower.service.exportexcel.EmployeeExportExcelService;
import com.example.itspower.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EmployeeGroupServiceImpl implements EmployeeGroupService {
    @Autowired
    EmployeeGroupRepository groupRepository;
    @Autowired
    EmployeeExportExcelService employeeExportExcelService;

    @Override
    public void saveAll(List<addUserRequest> addUser) {
        List<EmployeeGroupEntity> save = new ArrayList<>();
        for (addUserRequest addUserRequest : addUser) {
            EmployeeGroupEntity employeeGroup = new EmployeeGroupEntity();
            employeeGroup.setId(addUserRequest.getId());
            List<EmployeeGroupEntity> entity = groupRepository.findLaborCode(addUserRequest.getLaborCode());
            if (entity.size() >0) {
                throw new RuntimeException("labor code duplicate");
            }
            employeeGroup.setGroupId(addUserRequest.getGroupId());
            employeeGroup.setLaborCode(addUserRequest.getLaborCode());
            employeeGroup.setName(addUserRequest.getName());
            employeeGroup.setCreateDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
            save.add(employeeGroup);
        }
        groupRepository.saveAll(save);
    }

    @Override
    public byte[] exportExcel() throws IOException {
         List<EmployeeExportExcel> exportExcels = groupRepository.getExcelEmployee();
         employeeExportExcelService.initializeData(exportExcels);
        return employeeExportExcelService.export();

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
