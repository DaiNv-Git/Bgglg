package com.example.itspower.service;

import com.example.itspower.model.resultset.UserDto;
import com.example.itspower.request.search.UserSearchRequest;
import com.example.itspower.request.userrequest.UserUpdateRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.search.UserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    SuccessResponse<Object> save(UserRequest userRequest);

    ResponseEntity<Object> update(UserUpdateRequest userUpdateRequest, int id);

    UserDto loginInfor(String userLogin);

    void delete(Integer id);

    boolean isCheckReport(int groupId);
    PageResponse getAllUser(UserSearchRequest request, int pageSize, int pageNo);
}
