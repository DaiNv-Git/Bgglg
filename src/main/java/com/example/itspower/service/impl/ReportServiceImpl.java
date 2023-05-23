package com.example.itspower.service.impl;
import com.example.itspower.model.entity.*;
import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.model.resultset.RestDto;
import com.example.itspower.repository.*;
import com.example.itspower.repository.repositoryjpa.EmpTerminationContractRepository;
import com.example.itspower.repository.repositoryjpa.EmployeeGroupRepository;
import com.example.itspower.repository.repositoryjpa.ReportJpaRepository;
import com.example.itspower.request.ReportRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.report.ReportResponse;
import com.example.itspower.response.report.ReportSearchResponse;
import com.example.itspower.service.ReportService;
import com.example.itspower.util.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportJpaRepository reportJpaRepository;
    private final RestRepository restRepository;
    private final TransferRepository transferRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final RiceRepository riceRepository;
    private final EmpTerminationContractRepository empTerminationContractRepository;
    private final EmployeeGroupRepository employeeGroupRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ReportJpaRepository reportJpaRepository, RestRepository restRepository, TransferRepository transferRepository, GroupRoleRepository groupRoleRepository, RiceRepository riceRepository, EmpTerminationContractRepository empTerminationContractRepository, EmployeeGroupRepository employeeGroupRepository) {
        this.reportRepository = reportRepository;
        this.reportJpaRepository = reportJpaRepository;
        this.restRepository = restRepository;
        this.transferRepository = transferRepository;
        this.groupRoleRepository = groupRoleRepository;
        this.riceRepository = riceRepository;
        this.empTerminationContractRepository = empTerminationContractRepository;
        this.employeeGroupRepository = employeeGroupRepository;
    }

    @Override
    public Object reportDto(String reportDate, int groupId) {
        Optional<ReportEntity> entity = reportRepository.findByReportDateAndGroupId(reportDate, groupId);
        if (entity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "report is not exits now date", HttpStatus.INTERNAL_SERVER_ERROR.name()));
        }
        ReportDto reportDto = reportRepository.reportDto(reportDate, groupId);
        List<RestDto> restDtos = restRepository.getRests(reportDto.getId());
        List<TransferEntity> transferEntities = transferRepository.findByReportId(reportDto.getId());
        Optional<RiceEntity> riceEntity = riceRepository.getByRiceDetail(reportDto.getId());
        return new ReportResponse(reportDto, riceEntity.get(), restDtos, transferEntities);
    }

    @Override
    public Object search(String reportDate, int groupId) {
        try{
            ReportSearchResponse response= reportJpaRepository.searchReport(reportDate,groupId);
            if(response !=null){
                response.setEmployeeReceive(reportJpaRepository.employeeReceive(reportDate,groupId));
                response.setEmployeeStop(reportJpaRepository.findEmployeeStop(reportDate,groupId));
            }
            return response;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object callDataByDate(int groupId) {
        List<ReportEntity> data = reportJpaRepository.findLastDate(groupId);
        if (!data.isEmpty()) {
            String date = DateUtils.formatDate(data.get(0).getReportDate(), DateUtils.FORMAT_DATE);
            return getYesterday(date, groupId);
        }
        return null;
    }

    public Object getYesterday(String reportDate, int groupId) {
        try {
            Optional<ReportEntity> entity = reportRepository.findByReportDateAndGroupId(reportDate, groupId);
            if (entity.isEmpty()) {
                return null;
            }
            ReportDto reportDto = reportRepository.reportDto(reportDate, groupId);
            List<RestDto> restDtos = restRepository.getRests(reportDto.getId());
            List<TransferEntity> transferEntities = transferRepository.findByReportId(reportDto.getId());
            Optional<RiceEntity> riceEntity = riceRepository.getByRiceDetail(reportDto.getId());
            return new ReportResponse(reportDto, riceEntity.get(), restDtos, transferEntities);
        } catch (Exception e) {
            throw new RuntimeException("get pesterDay fail");
        }
    }

    @Override
    public void save(ReportRequest request, int groupId) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 7);
            Date newDate = calendar.getTime();
            Optional<ReportEntity> entity = reportRepository.findByReportDateAndGroupId(DateUtils.formatDate(newDate), groupId);
            if (entity.isPresent()) {
                throw new RuntimeException(" report date is exits");
            }
            ReportEntity reportEntity = reportRepository.saveReport(request, groupId);
            riceRepository.saveRice(request.getRiceRequests(), reportEntity.getId());
            restRepository.saveRest(request.getRestRequests(), reportEntity.getId());
            transferRepository.saveTransfer(request.getTransferRequests(), reportEntity.getId(),groupId);
        } catch (Exception e) {
            throw new RuntimeException("save fail");
        }
    }

    @Override
    public void update(ReportRequest request, int groupId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        Date newDate = calendar.getTime();
        Optional<ReportEntity> entity = reportRepository.findByReportDateAndGroupId(DateUtils.formatDate(newDate), groupId);
        if (entity.isEmpty()) {
            throw new RuntimeException(" report date is not exits");
        }
        ReportEntity reportEntity = updateReport(request, groupId);
    }

    public ReportEntity updateReport(ReportRequest request, int groupId) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setId(request.getId());
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
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        Date newDate = calendar.getTime();
        reportEntity.setReportDate(newDate);
        return reportJpaRepository.saveAndFlush(reportEntity);
    }
    @Override
    public void deleteRestIdsAndReportId(Integer reportId, List<Integer> restIds) {
        restRepository.deleteRestIdsAndReportId(reportId, restIds);
    }

    public void deleteRestEmployee(Integer groupId, List<String> laborEmps) {
        try {
            LocalDateTime now = LocalDateTime.now().plusHours(7);
            Date newDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            Optional<GroupEntity> groupEntity = groupRoleRepository.findById(groupId);
            if (groupEntity.isPresent()) {
                addEmpTerminationContract(groupId, laborEmps, newDate);
                employeeGroupRepository.deleteByGroupIdAndLaborCodeIn(groupId, laborEmps);
                groupEntity.get().setDemarcationAvailable(groupEntity.get().getDemarcationAvailable() - laborEmps.size());
                groupRoleRepository.save(groupEntity.get());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Integer getTransfer(String reportDate, Integer groupId) {
        return reportJpaRepository.getTransferNumer(reportDate, groupId);
    }

    @Override
    public List<Integer> getIdsToMay() {
        return reportJpaRepository.getIDsTomay();
    }

    @Override
    public byte[] exportExcel() throws IOException {
        return new byte[0];
    }

    private void addEmpTerminationContract(Integer groupId, List<String> laborEmps, Date date) {
        try {
            if (laborEmps.size() != 0) {
                List<EmployeeTerminationOfContractEntity> entities = new ArrayList<>();
                for (String laborEmp : laborEmps) {
                    Optional<EmployeeGroupEntity> employeeGroup = employeeGroupRepository.findByLaborCode(laborEmp);
                    if (employeeGroup.isPresent()) {
                        EmployeeGroupEntity groupEntity = employeeGroup.get();
                        EmployeeTerminationOfContractEntity entityTerOfContract = new EmployeeTerminationOfContractEntity();
                        entityTerOfContract.setEmployeeLabor(groupEntity.getLaborCode());
                        entityTerOfContract.setEmployeeName(groupEntity.getName());
                        entityTerOfContract.setGroupId(groupId);
                        entityTerOfContract.setStartDate(date);
                        entities.add(entityTerOfContract);
                    }
                }
                empTerminationContractRepository.saveAll(entities);
            }
        } catch (Exception e) {
            throw new RuntimeException("addEmpTerminationContract fail");
        }
    }
}