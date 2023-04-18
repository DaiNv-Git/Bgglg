package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.UserEntity;
import com.example.itspower.model.resultset.UserDto;
import com.example.itspower.response.user.ListUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByUserLogin(String userName);

    @Query(name = "loginInfor", nativeQuery = true)
    UserDto loginInfor(@Param("userLogin") String userLogin);

    @Query(name = "list_infor_user", nativeQuery = true)
    List<ListUserResponse> listUser(@Param("groupName") String groupName
            , @Param("loginName") String loginName
            , @Param("pageSize") int pageSize
            , @Param("pageNo") int pageNo);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user WHERE id = :ids", nativeQuery = true)
    void deleteByIds(@Param("ids") Integer ids);

    @Query(value = "select count(*) from user ", nativeQuery = true)
    int countUser();
}