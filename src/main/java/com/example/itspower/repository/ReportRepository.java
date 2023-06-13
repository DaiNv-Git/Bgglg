package com.example.itspower.repository;

import com.example.itspower.model.entity.ReportEntity;
import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.repository.repositoryjpa.ReportJpaRepository;
import com.example.itspower.request.ReportRequest;
import com.example.itspower.response.export.ExportExcelDtoReport;
import com.example.itspower.response.export.ExportExcelEmpRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Component
public class ReportRepository {
    @Autowired
      private ReportJpaRepository reportJpaRepository;

    public ReportDto reportDto(String reportDate, int groupId) {
           return reportJpaRepository.findByReport(reportDate, groupId);
    }

     public List<ExportExcelDtoReport> findByReportExcel(String reportDate) {
        return reportJpaRepository.findByReportExcel(reportDate);
    }

    public List<ExportExcelEmpRest> findByReportExcelEmpRest(String reportDate) {
         return reportJpaRepository.findByReportExcelEmpRest(reportDate);
    }

    public ReportEntity saveReport(ReportRequest request, int groupId) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setDemarcation(request.getDemarcation());
        reportEntity.setGroupId(groupId);
        reportEntity.setDemarcationAvailable(request.getDemarcationAvailable());
        reportEntity.setRestNum(request.getRestNum());
        reportEntity.setStudentNum(request.getStudentNum());
        reportEntity.setLaborProductivity(request.getLaborProductivity());
        reportEntity.setUnproductiveLabor(request.getUnproductiveLabor());
        reportEntity.setPartTimeNum(request.getPartTimeNum());
        reportEntity.setProfessionLabor(request.getProfessionLabor());
        reportEntity.setProfessionNotLabor(request.getProfessionNotLabor());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // yourDate là thời gian hiện tại của bạn
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
        Date newDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(newDate);
        reportEntity.setReportDate(newDate);
        reportEntity.setReportDateFix(String.valueOf(newDate));
        reportJpaRepository.save(reportEntity);
        return reportJpaRepository.findByReportDateAndGroupId(dateString,groupId).get();
    }


    public Optional<ReportEntity> findByReportDateAndGroupId(String reportDate, int groupId) {
         return reportJpaRepository.findByReportDateAndGroupId(reportDate, groupId);
    }

    public Optional<ReportEntity> findByIdAndGroupId(int id, int groupId) {
        return reportJpaRepository.findByIdAndGroupId(id, groupId);
    }
}
