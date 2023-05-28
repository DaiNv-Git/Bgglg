package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.EmployeeGroupEntity;
import com.example.itspower.response.employee.EmployeeExportExcel;
import com.example.itspower.response.employee.EmployeeGroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeGroupRepository extends JpaRepository<EmployeeGroupEntity, Integer> {
    @Query(name = "view_all_employee", nativeQuery = true)
    List<EmployeeGroupResponse> getEmployee(@Param("groupId") Integer groupId
            , @Param("groupName") String groupName
            , @Param("laborCode") String laborCode
            , @Param("employeeName") String employeeName
            , @Param("pageSize") int pageSize, @Param("pageNo") int pageNo);

    @Query(value = "SELECT count(*) from group_employee", nativeQuery = true)
    int countEmployee();


    @Query(value = "SELECT * from group_employee ge where labor_code = ?1",nativeQuery = true)
    Integer findIDByLaborCode(String laborCode);

    @Query(name = "execl_Employee", nativeQuery = true)
    List<EmployeeExportExcel> getExcelEmployee();
    @Transactional
    @Modifying
    void deleteByGroupIdAndLaborCodeIn(Integer groupId,List<String> laborCode);

    Optional<EmployeeGroupEntity> findByLaborCode(String laborCode);
    @Query(value = "select * from group_employee where labor_code = ?1",nativeQuery = true)
    List<EmployeeGroupEntity> findLaborCode(String laborCode);

    @Query(value = "select * from group_employee ge where labor_code = ?1 and ge.id <> ?2",nativeQuery = true)
    List<EmployeeGroupEntity> findLaborCodeUpdate(String laborCode ,Integer id);
}
