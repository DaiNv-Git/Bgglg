package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.ReportEntity;
import com.example.itspower.model.resultset.ReportDto;
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

    List<String> findEmployeeStop(@Param("reportDate") String reportDate, @Param("groupId") int groupId);


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
