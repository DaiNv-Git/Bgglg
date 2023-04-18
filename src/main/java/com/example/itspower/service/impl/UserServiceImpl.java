package com.example.itspower.service.impl;

import com.example.itspower.component.util.DateUtils;
import com.example.itspower.exception.ResourceNotFoundException;
import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.entity.ReportEntity;
import com.example.itspower.model.entity.UserEntity;
import com.example.itspower.model.entity.UserGroupEntity;
import com.example.itspower.model.resultset.UserDto;
import com.example.itspower.repository.*;
import com.example.itspower.repository.repositoryjpa.UserJpaRepository;
import com.example.itspower.request.search.UserSearchRequest;
import com.example.itspower.request.userrequest.UserUpdateRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.search.UserRequest;
import com.example.itspower.response.user.ListUserResponse;
import com.example.itspower.response.user.UserResponseSave;
import com.example.itspower.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final ReportRepository reportRepository;
    private final RestRepository restRepository;
    private final RiceRepository riceRepository;
    private final TransferRepository transferRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserGroupRepository userGroupRepository;
    private final UserLoginConfig userLoginConfig;

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public SuccessResponse<Object> save(UserRequest userRequest) {
        Optional<UserEntity> userEntity = userRepository.findByUserLogin(userRequest.getUserLogin());
        if (userEntity.isPresent()) {
            return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "UserLogin is exits", null);
        }
        Optional<GroupEntity> groupEntity = groupRoleRepository.findById(userRequest.getGroupId());
        if (groupEntity.isEmpty()) {
            return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "group Child is empty!", null);
        }
        UserEntity user = new UserEntity();
        user.setUserLogin(userRequest.getUserLogin());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEdit(userRequest.isEdit());
        user.setView(userRequest.isView());
        user.setReport(userRequest.isReport());
        user.setAdmin(userRequest.isAdmin());
        user = userRepository.save(user);
        UserGroupEntity userGroupEntity = userGroupRepository.save(user.getId(), groupEntity.get().getId());
        UserResponseSave save = new UserResponseSave(user, groupEntity, userGroupEntity);
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "register success", save);

    }


    @Override
    @Transactional
    public ResponseEntity<Object> update(UserUpdateRequest userUpdateRequest, int id) {
        try {
            UserDetails userEntity = userLoginConfig.loadUserById(id);
            UserEntity user = userJpaRepository.findById(id).get();
            Optional<UserGroupEntity> userGroupEntity = userGroupRepository.finByUserId(id);
            Optional<GroupEntity> groupEntity = groupRoleRepository.findById(userGroupEntity.get().getGroupId());
            user.setId(id);
            user.setUserLogin(userEntity.getUsername());
            user.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            user.setEdit(userUpdateRequest.isEdit());
            user.setView(userUpdateRequest.isView());
            user.setReport(userUpdateRequest.isReport());
            user.setAdmin(userUpdateRequest.isAdmin());
            user = userRepository.save(user);
            return ResponseEntity.ok().body(new UserResponseSave(user, groupEntity, userGroupEntity.get()));
        } catch (Exception e) {
            throw new ResourceNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            userGroupRepository.deleteGroupUser(id);
            userRepository.deleteIds(id);
        } catch (Exception e) {
            throw new RuntimeException("delete not success");
        }
    }

    public boolean isCheckReport(int groupId) {
        Optional<ReportEntity> reportEntity = reportRepository.findByReportDateAndGroupId(DateUtils.formatDate(new Date()), groupId);
        return reportEntity.isPresent();
    }

    @Override
    public PageResponse getAllUser(UserSearchRequest request, int pageSize, int pageNo) {
        int offset = (pageNo - 1) * pageSize;
        int countUsers = userJpaRepository.countUser();
        List<ListUserResponse> resUser = userJpaRepository.listUser(request.getGroupName(), request.getUserName(), pageSize, offset);
        Pageable pageable = PageRequest.of(offset, pageSize);
        final Page<ListUserResponse> page = new PageImpl<>(resUser, pageable, 0);
        return new PageResponse<>(page, (long) countUsers);
    }

    public UserDto loginInfor(String userLogin) {
        return userRepository.loginInfor(userLogin);
    }
}
