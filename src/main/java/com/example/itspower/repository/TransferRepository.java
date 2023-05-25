package com.example.itspower.repository;

import com.example.itspower.model.entity.*;
import com.example.itspower.repository.repositoryjpa.*;
import com.example.itspower.request.TransferRequest;
import com.example.itspower.response.transfer.TransferNumAccept;
import com.example.itspower.response.transfer.TransferResponseGroup;
import com.example.itspower.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class TransferRepository {
    @Autowired
    private TransferJpaRepository transferJpaRepository;
    @Autowired
    private GroupJpaRepository groupJpaRepository;
    @Autowired
    private EmployeeTransferRepository transferRepository;
    @Autowired
    EmployeeGroupRepository employeeGroupRepository;

    @Autowired
    ReportJpaRepository reportJpaRepository;

    @Autowired
    ReportRepository reportRepository;

    public List<TransferEntity> findByReportId(Integer reportId) {
         List<TransferEntity> entities = transferJpaRepository.findByReportId(reportId);
        return entities;
    }
    @Transactional
    public Object updateTransfer(List<TransferRequest> requests, Integer reportId) {
        List<TransferEntity> entities = new ArrayList<>();
        for (TransferRequest transfer : requests) {
            TransferEntity entity = new TransferEntity();
            entity.setTransferId(transfer.getTransferId());
            entity.setReportId(reportId);
            entity.setGroupId(transfer.getGroupId());
            entity.setTransferDate(new Date());
            entity.setTransferNum(transfer.getTransferNum());
            entities.add(entity);
        }
        return transferJpaRepository.saveAll(entities);
    }
    public void saveTransfer(List<TransferRequest> requests, Integer reportId,int groupId) {
        try{
            List<TransferEntity> entities = new ArrayList<>();
            List<EmployeeTransferEntity> employees = new ArrayList<>();
//            List<EmployeeGroupEntity> employeeGroupEntities =employeeGroupRepository.findAll();
            List<EmployeeGroupEntity> updateGroupEmployee =new ArrayList<>();
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime newDateTime = currentTime.plus(7, ChronoUnit.HOURS);
            Date newDate = java.sql.Timestamp.valueOf(newDateTime);
            String date = DateUtils.formatDate(newDate,DateUtils.FORMAT_DATE);
            for (TransferRequest transfer : requests) {
                TransferEntity entity = new TransferEntity();
                entity.setReportId(reportId);
                entity.setGroupId(transfer.getGroupId());
                entity.setTransferDate(new Date());
                entity.setTransferNum(transfer.getTransferNum());
                entities.add(entity);
                Optional<GroupEntity> changeDinhBien = groupJpaRepository.findById(transfer.getGroupId());
                Optional<GroupEntity> changeDinhBienRoot = groupJpaRepository.findById(groupId);
                Optional<ReportEntity> report = reportRepository.findByReportDateAndGroupId(date,transfer.getGroupId());
                Optional<ReportEntity> reportRoot = reportRepository.findByReportDateAndGroupId(date,groupId);
                if(changeDinhBien.isPresent()){
                    float dinhBien = changeDinhBien.get().getDemarcationAvailable() +transfer.getEmployees().size();
                    changeDinhBien.get().setDemarcationAvailable(dinhBien);
                    groupJpaRepository.save(changeDinhBien.get());
                }
                if(changeDinhBienRoot.isPresent()){
                    float dinhBien = changeDinhBienRoot.get().getDemarcationAvailable() - transfer.getEmployees().size();
                    changeDinhBienRoot.get().setDemarcationAvailable(dinhBien);
                    groupJpaRepository.save(changeDinhBienRoot.get());
                }
                if(report.isPresent()){
                    float dinhBien = report.get().getDemarcation() + transfer.getEmployees().size();
                    report.get().setDemarcation(dinhBien);
                    reportJpaRepository.save(report.get());
                }
                if(reportRoot.isPresent()){
                    float dinhBien = reportRoot.get().getDemarcation() - transfer.getEmployees().size();
                    reportRoot.get().setDemarcation(dinhBien);
                    reportJpaRepository.save(reportRoot.get());
                }
                for(String employeeTransfer :transfer.getEmployees()){
                    int index = employeeTransfer.indexOf(" - ");
                    String name = null;
                    String labor;
                    if (index != -1) {
                         name = employeeTransfer.substring(0, index);
                         labor = employeeTransfer.substring(index + 3);
                    } else {
                        labor = null;
                    }
                    EmployeeTransferEntity employeeTransferSave = new EmployeeTransferEntity();
                    employeeTransferSave.setName(name);
                    employeeTransferSave.setGroupID(transfer.getGroupId());
                    employeeTransferSave.setTransferDate(DateUtils.formatDate(new Date(),DateUtils.FORMAT_DATE));
                    employeeTransferSave.setLabor(labor);
                    employees.add(employeeTransferSave);
                    EmployeeGroupEntity employeeGroup = new EmployeeGroupEntity();
                    Integer employeeID = employeeGroupRepository.findIDByLaborCode(labor);
                    if(employeeID !=null){
                        employeeGroup.setId(employeeID);
                    }
                    employeeGroup.setLaborCode(labor);
                    employeeGroup.setName(name);
                    employeeGroup.setGroupId(transfer.getGroupId());
                    updateGroupEmployee.add(employeeGroup);
                }
            }
            employeeGroupRepository.saveAll(updateGroupEmployee);
            transferRepository.saveAll(employees);
            transferJpaRepository.saveAll(entities);
        }catch (Exception e){
            throw new RuntimeException("update fail");
        }

    }


    public List<TransferResponseGroup> findGroupIdAndTransferDate(int groupId,String reportDate) throws ParseException {
        Date date=new SimpleDateFormat("yyyy/MM/dd").parse(reportDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // yourDate là thời gian hiện tại của bạn
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
        Date newDate = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(newDate);
        List<TransferNumAccept> entities = transferJpaRepository.findGroupIdAndTransferDate(groupId, strDate);
        List<TransferResponseGroup> transferResponseGroups = new ArrayList<>();
        for (TransferNumAccept transsfer : entities) {
            transferResponseGroups.add(new TransferResponseGroup(transsfer.getId(), transsfer.getGroupName(), transsfer.getTransferNum()));
        }
        return transferResponseGroups;
    }

    public void updateTransferGroup(boolean isAccess, int groupId, String transferDate) {
        transferJpaRepository.updateTransfer(isAccess, groupId, transferDate);
    }

    @Transactional
    @Modifying
    public void deleteTransferReportId(Integer reportId) {
        transferJpaRepository.deleteByReportId(reportId);
    }
}
