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
import com.example.itspower.service.ReportService;
import com.example.itspower.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportJpaRepository reportJpaRepository;
    @Autowired
    private RestRepository restRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private GroupRoleRepository groupRoleRepository;
    @Autowired
    private RiceRepository riceRepository;
    @Autowired
    private EmpTerminationContractRepository empTerminationContractRepository;
    @Autowired
    private EmployeeGroupRepository employeeGroupRepository;

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
    public Object callDataByDate(int groupId) {
        List<ReportEntity> data = reportJpaRepository.findLastDate(groupId);
        if(!data.isEmpty()){
            String date =DateUtils.formatDate(data.get(0).getReportDate(),DateUtils.FORMAT_DATE);
            return getYesterday(date,groupId);
        }
        return null;
    }
    public Object getYesterday(String reportDate, int groupId) {
        Optional<ReportEntity> entity = reportRepository.findByReportDateAndGroupId(reportDate, groupId);
        if (entity.isEmpty()) {
            return null;
        }
        ReportDto reportDto = reportRepository.reportDto(reportDate, groupId);
        List<RestDto> restDtos = restRepository.getRests(reportDto.getId());
        List<TransferEntity> transferEntities = transferRepository.findByReportId(reportDto.getId());
        Optional<RiceEntity> riceEntity = riceRepository.getByRiceDetail(reportDto.getId());
        return new ReportResponse(reportDto, riceEntity.get(), restDtos, transferEntities);
    }
    @Override
    public Object save(ReportRequest request, int groupId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
        Date newDate = calendar.getTime();
        Optional<ReportEntity> entity = reportRepository.findByReportDateAndGroupId(DateUtils.formatDate(newDate), groupId);
        if (entity.isPresent()) {
            return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "report date is exits", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        ReportEntity reportEntity = reportRepository.saveReport(request, groupId);
        riceRepository.saveRice(request.getRiceRequests(), reportEntity.getId());
        restRepository.saveRest(request.getRestRequests(), reportEntity.getId());
        transferRepository.saveTransfer(request.getTransferRequests(), reportEntity.getId());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.CREATED.value(), "report success", reportDto(DateUtils.formatDate(reportEntity.getReportDate()), reportEntity.getGroupId())));
    }

    @Override
    @Transactional
    public Object update(ReportRequest request, int groupId) {
        Optional<ReportEntity> entity = reportRepository.findByIdAndGroupId(request.getId(), groupId);
        if (entity.isEmpty()) {
            return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "report is not Exits", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        ReportEntity reportEntity = reportRepository.updateReport(request, groupId);
        if (request.getRiceRequests().getRiceId() != null && request.getRiceRequests().getRiceId() != 0) {
            riceRepository.updateRice(request.getRiceRequests(), reportEntity.getId());
        }
        request.getRestRequests().forEach(z -> {
                restRepository.updateRest(request.getRestRequests(), reportEntity.getId());
        });
        request.getTransferRequests().forEach(i -> {
            if (i.getTransferId() != null && i.getTransferId() != 0) {
                transferRepository.updateTransfer(request.getTransferRequests(), reportEntity.getId());
            }
        });
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK.value(), "update report success", reportDto(DateUtils.formatDate(reportEntity.getReportDate()), reportEntity.getGroupId())));
    }

    @Override
    public void deleteRestIdsAndReportId(Integer reportId, List<Integer> restIds) {
        restRepository.deleteRestIdsAndReportId(reportId, restIds);
    }


    public void deleteRestEmployee(Integer groupId, List<String> laborEmps) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
        Date newDate = calendar.getTime();
        Optional<GroupEntity> groupEntity = groupRoleRepository.findById(groupId);
        if (groupEntity.isPresent()) {
            addEmpTerminationContract(groupId, laborEmps, newDate);
            employeeGroupRepository.deleteByGroupIdAndLaborCodeIn(groupId, laborEmps);
            groupEntity.get().setDemarcationAvailable(groupEntity.get().getDemarcationAvailable() - laborEmps.size());
            groupRoleRepository.save(groupEntity.get());
        }
    }

    private void addEmpTerminationContract(Integer groupId, List<String> laborEmps, Date date) {
        if (laborEmps.size() != 0) {
            List<EmployeeTerminationOfContractEntity> entities = new ArrayList<>();
            for (String laborEmp : laborEmps) {
                Optional<EmployeeGroupEntity> employeeGroup = employeeGroupRepository.findByLaborCode(laborEmp);
                if (employeeGroup.isPresent()) {
                    EmployeeTerminationOfContractEntity entityTerOfContract = new EmployeeTerminationOfContractEntity();
                    entityTerOfContract.setEmployeeLabor(employeeGroup.get().getLaborCode());
                    entityTerOfContract.setEmployeeName(employeeGroup.get().getName());
                    entityTerOfContract.setGroupId(groupId);
                    entityTerOfContract.setStartDate(date);
                    entities.add(entityTerOfContract);
                }
            }
            empTerminationContractRepository.saveAll(entities);
        }
    }
}