package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.resultset.GroupRoleDto;
import com.example.itspower.model.resultset.RootNameDto;
import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.response.group.ViewDetailGroupResponse;
import com.example.itspower.response.group.ViewGroupRoot;
import com.example.itspower.response.view.ListNameRestResponse;
import com.example.itspower.response.view.ReasonResponse;
import com.example.itspower.response.view.RootResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJpaRepository extends JpaRepository<GroupEntity, Integer> {
    List<GroupEntity> findAllByParentId(int parentId);
    @Query(value = "select * from group_role where group_name like concat('%',:groupName,'%') LIMIT :pageSize OFFSET :pageNo ", nativeQuery = true)
    List<GroupEntity> findByGroupName(@Param("groupName") String groupName
            ,@Param("pageSize") int pageSize
            ,@Param("pageNo")  int pageNo);
    @Query(value = "select count(*) from group_role", nativeQuery = true)
    int countGroupRole();
    @Transactional
    @Modifying
    @Query(value = "Delete FROM group_role where id =:groupId ", nativeQuery = true)
    void deleteByGroupName(@Param("groupId") Integer groupId);
    List<GroupEntity> findAllByParentIdIsNull();
    @Query(name = "findAllRole", nativeQuery = true)
    List<GroupRoleDto> findAllRole();
    @Query(name = "findAllRoleView", nativeQuery = true)
    List<ViewAllDto> findAllViewRole(@Param("reportDate") String reportDate);
    @Query(name = "findAllRoot", nativeQuery = true)
    List<RootNameDto> getAllRoot();
    @Query(name = "findRoot", nativeQuery = true)
    List<RootResponse> getRoot();
    @Query(value = "SELECT group_name  from group_role gr ", nativeQuery = true)
    List<String> getAllByGroupName();
    @Query(value = "select distinct gr.parent_id from group_role gr where parent_id is not null ", nativeQuery = true)
    List<Integer> getAllParentId();
    @Query(name = "findByViewDetail", nativeQuery = true)
    List<ViewDetailGroupResponse> getDetail(@Param("reportDate") String reportDate);
    @Query(name = "findByViewDetailParent", nativeQuery = true)
    List<ViewDetailGroupResponse> getDetailParent();
    @Query(name = "view_group_root", nativeQuery = true)
    List<ViewGroupRoot> getViewGroup();
    List<GroupEntity> findByGroupNameIn(List<String> groupName);
    Optional<GroupEntity> findByGroupName(String groupName);
    Optional<GroupEntity> findByGroupNameAndParentId(String groupName, Integer parentId);
    @Query(name = "view_reason", nativeQuery = true)
    List<ReasonResponse>  getReasonResponse(String reportDate);
    @Query(value ="SELECT sum(student_num) from report r where  DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:#{#reportDate}, '%Y%m%d')", nativeQuery = true)
    Integer getStudentNumber(String reportDate);
    @Query(name = "view_list_reason", nativeQuery = true)
    List<ListNameRestResponse>  getListNameReason(String reportDate);
    @Query(value = " SELECT IFNULL(MAX(sort),0)  from group_role gr  ",nativeQuery = true)
    Integer getMaxSort();
}
