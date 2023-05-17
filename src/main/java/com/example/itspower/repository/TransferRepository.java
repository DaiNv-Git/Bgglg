package com.example.itspower.repository;

import com.example.itspower.model.entity.EmployeeTransferEntity;
import com.example.itspower.model.entity.TransferEntity;
import com.example.itspower.repository.repositoryjpa.EmployeeTransferRepository;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.repository.repositoryjpa.TransferJpaRepository;
import com.example.itspower.request.TransferRequest;
import com.example.itspower.response.employee.EmployeeInforResponse;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TransferRepository {
    @Autowired
    private TransferJpaRepository transferJpaRepository;
    @Autowired
    private GroupJpaRepository groupJpaRepository;
    @Autowired
    private EmployeeTransferRepository transferRepository;

    public List<TransferEntity> findByReportId(Integer reportId) {
         List<TransferEntity> entities = transferJpaRepository.findByReportId(reportId);
        return entities;
    }

    public Object saveTransfer(List<TransferRequest> requests, Integer reportId) {
        List<TransferEntity> entities = new ArrayList<>();
         List<EmployeeTransferEntity> employees = new ArrayList<>();
        for (TransferRequest transfer : requests) {
            TransferEntity entity = new TransferEntity();
            entity.setReportId(reportId);
            entity.setGroupId(transfer.getGroupId());
            entity.setTransferDate(new Date());
            entity.setTransferNum(transfer.getTransferNum());
            entity.setType(transfer.getType());
            entities.add(entity);
            for(EmployeeInforResponse employeeTransfer :transfer.getEmployees()){
                EmployeeTransferEntity employeeTransferSave = new EmployeeTransferEntity();
                employeeTransferSave.setTransferType(transfer.getType());
                employeeTransferSave.setName(employeeTransfer.getName());
                employeeTransferSave.setGroupID(transfer.getGroupId());
                employeeTransferSave.setTransferDate(DateUtils.formatDate(new Date(),DateUtils.FORMAT_DATE));
                employeeTransferSave.setLabor(employeeTransfer.getLabor());
                employees.add(employeeTransferSave);
            }
        }
        transferRepository.saveAll(employees);
        return transferJpaRepository.saveAll(entities);
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
            entity.setType(transfer.getType());
            entities.add(entity);
        }
        return transferJpaRepository.saveAll(entities);
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
