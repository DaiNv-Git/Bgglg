package com.example.itspower.service;

import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.dynamic.PageResponse;

import java.util.List;

public interface EmployeeGroupService {
    void saveAll(List<addUserRequest> addUser);

    void delete(List<Integer> ids);

    PageResponse getEmployee(String groupName, Integer groupId, String laborCode, String employeeName, int pageSize, int pageNo);
}
