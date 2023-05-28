package com.example.itspower.service;

import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.dynamic.PageResponse;

import java.io.IOException;
import java.util.List;

public interface EmployeeGroupService {
    void saveAll(List<addUserRequest> addUser);
     byte[] exportExcel() throws IOException;


    void delete(List<Integer> ids);

    PageResponse getEmployee(String groupName, Integer groupId, String laborCode, String employeeName, int pageSize, int pageNo);
}
