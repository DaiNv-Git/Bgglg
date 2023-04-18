package com.example.itspower.repository;

import com.example.itspower.model.entity.UserEntity;
import com.example.itspower.model.resultset.UserDto;
import com.example.itspower.repository.repositoryjpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepository {
    @Autowired
    private UserJpaRepository userJPARepository;



    public UserEntity save(UserEntity user) {
        return userJPARepository.save(user);
    }

    public Optional<UserEntity> findByUserLogin(String userLogin) {
        return userJPARepository.findByUserLogin(userLogin);
    }

    public Optional<UserEntity> findByUserId(int id) {
        return userJPARepository.findById(id);
    }

    public void deleteIds(Integer ids) {
         userJPARepository.deleteByIds(ids);
    }

    public UserDto loginInfor(String userLogin) {
        return userJPARepository.loginInfor(userLogin);
    }
}
