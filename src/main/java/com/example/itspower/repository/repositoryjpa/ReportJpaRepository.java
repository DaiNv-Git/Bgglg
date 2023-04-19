package com.example.itspower.repository.repositoryjpa;
import com.example.itspower.model.entity.ReportEntity;
import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.response.view.ViewDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ReportJpaRepository extends JpaRepository<ReportEntity, Integer>, JpaSpecificationExecutor<ReportEntity> {
    @Query(name = "find_by_report", nativeQuery = true)
    ReportDto findByReport(@Param("reportDate") String reportDate, @Param("groupId") int groupId);

    @Query(name = "get_view_report", nativeQuery = true)
    ViewDetailResponse viewRootReport(@Param("reportDate") String reportDate, @Param("parentId") int parentId);

    @Query(value = "select * from report r where  DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:#{#reportDate}, '%Y%m%d') ", nativeQuery = true)
    Optional<ReportEntity> findByReportDate(String reportDate);

    @Query(value = "select * from report r where " +
            " DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:#{#reportDate}, '%Y%m%d') and group_ID =:groupId", nativeQuery = true)
    Optional<ReportEntity> findByReportDateAndGroupId(String reportDate,@Param("groupId") Integer groupId);

    Optional<ReportEntity> findByIdAndGroupId(int id, int groupId);

    Optional<ReportEntity> findByGroupId(int groupId);

    @Transactional
    @Modifying
    void deleteByGroupId(Integer groupId);
}
