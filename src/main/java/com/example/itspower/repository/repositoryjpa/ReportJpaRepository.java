package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.ReportEntity;
import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.response.ReportNangsuatResponse;
import com.example.itspower.response.export.ExportExcelDtoReport;
import com.example.itspower.response.export.ExportExcelEmpRest;
import com.example.itspower.response.report.ReportSearchResponse;
import com.example.itspower.response.view.ViewDetailResponse;
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
public interface ReportJpaRepository extends JpaRepository<ReportEntity, Integer>, JpaSpecificationExecutor<ReportEntity> {
    @Query(name = "find_by_report", nativeQuery = true)
    ReportDto findByReport(@Param("reportDate") String reportDate, @Param("groupId") int groupId);

    @Query(name = "GroupReport.findAll", nativeQuery = true)
    List<ReportNangsuatResponse> findNangSuat(@Param("reportDate") String reportDate);


    @Query(value ="SELECT CONCAT(e.name, ' - ', e.labor)" +
            "FROM list_employee_transfer e " +
            "WHERE e.transfer_type  = (" +
            "        SELECT r2.id" +
            "        FROM report r2" +
            "        WHERE r2.group_id = ?2" +
            "        AND DATE_FORMAT(r2.report_date, '%Y%m%d') = DATE_FORMAT(?1, '%Y%m%d'))",nativeQuery = true)
    List<String> findEmployeeTransferTo(@Param("reportDate") String reportDate, @Param("groupId") int groupId);
    @Query(value = "SELECT  CONCAT(employee_name, ' - ', employee_labor) from emp_termination_contract etc " +
            "where etc.group_id =?2 and  DATE_FORMAT(etc.start_date, '%Y%m%d') = DATE_FORMAT(?1, '%Y%m%d')",nativeQuery = true)
    List<String> findEmployeeStop(@Param("reportDate") String reportDate, @Param("groupId") int groupId);
    @Query(value = "SELECT CONCAT(rest_name, ' - ', employee_labor, ' - ' , r2.name  ) as name " +
            "         from rest r  inner join reason r2 on r.reason_id =r2.id where r.report_id =" +
            " (SELECT id from report r3 where r3.group_id = ?2 and   DATE_FORMAT(r3.report_date, '%Y%m%d') = DATE_FORMAT(?1, '%Y%m%d') )",nativeQuery = true)
    List<String> findRestEmployee(@Param("reportDate") String reportDate, @Param("groupId") int groupId);



    @Query(value = "SELECT CONCAT(name, ' - ', labor) from list_employee_transfer w " +
            "where w.groupID =?2 and w.transfer_date =?1",nativeQuery = true)
    List<String> employeeReceive(@Param("reportDate") String reportDate, @Param("groupId") int groupId);

    @Query(name = "report_Search", nativeQuery = true)
    ReportSearchResponse searchReport(@Param("reportDate") String reportDate, @Param("groupId") int groupId);

    @Query(value = "SELECT SUM(transfer_num)   from transfer tr inner join report r on r.id =tr.report_id\n" +
            "where tr.group_id =?2 and  DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(?1, '%Y%m%d')", nativeQuery = true)
    Integer getTransferNumer(@Param("reportDate") String reportDate, @Param("groupId") int groupId);
    @Query(value = "SELECT id from group_role where parent_id in(SELECT id FROM group_role gr where " +
            "(group_name like '%Xí nghiệp 2' or group_name like '%Xí nghiệp 1' )" +
            "and parent_id = (SELECT id from group_role gr where group_name like '%tổ may'))", nativeQuery = true)
    List<Integer> getIDsTomay();

    @Query(name = "find_by_excel", nativeQuery = true)
    List<ExportExcelDtoReport> findByReportExcel(@Param("reportDate") String reportDate);

    @Query(name = "find_by_employee_rest", nativeQuery = true)
    List<ExportExcelEmpRest> findByReportExcelEmpRest(@Param("reportDate") String reportDate);

    @Query(name = "get_view_report", nativeQuery = true)
    ViewDetailResponse viewRootReport(@Param("reportDate") String reportDate, @Param("parentId") int parentId);

    @Query(value = "select * from report r where  DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:#{#reportDate}, '%Y%m%d') ", nativeQuery = true)
    Optional<ReportEntity> findByReportDate(String reportDate);

    @Query(value = "select * from report r where  DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') AND r.group_id = :groupId ", nativeQuery = true)
    Optional<ReportEntity> findByReportDateAndGroupId(@Param("reportDate") String reportDate, @Param("groupId") int groupId);

    Optional<ReportEntity> findByIdAndGroupId(int id, int groupId);

    @Query(value = "SELECT *  from report r where group_id = ?1 order by report_date desc ", nativeQuery = true)
    List<ReportEntity> findLastDate(int groupId);

    Optional<ReportEntity> findByGroupId(int groupId);

    @Transactional
    @Modifying
    void deleteByGroupId(Integer groupId);
}
