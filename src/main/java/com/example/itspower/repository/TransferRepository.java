package com.example.itspower.repository;

import com.example.itspower.component.util.DateUtils;
import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.entity.TransferEntity;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.repository.repositoryjpa.TransferJpaRepository;
import com.example.itspower.request.TransferRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.transfer.TransferResponseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class TransferRepository {
    @Autowired
    private TransferJpaRepository transferJpaRepository;
    @Autowired
    private GroupJpaRepository groupJpaRepository;

    public List<TransferEntity> findByReportId(Integer reportId) {
        List<TransferEntity> entities = transferJpaRepository.findByReportId(reportId);
        return entities;
    }

    public Object saveTransfer(List<TransferRequest> requests, Integer reportId, Integer groupId) {
        List<TransferEntity> entities = new ArrayList<>();
        for (TransferRequest transfer : requests) {
            if (groupId != null && groupId == transfer.getGroupId()) {
                return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "groupId is not crrurent groupId user", null);
            }
            TransferEntity entity = new TransferEntity();
            entity.setReportId(reportId);
            entity.setGroupId(transfer.getGroupId());
            entity.setTransferDate(new Date());
            entity.setTransferNum(transfer.getTransferNum());
            entity.setType(transfer.getType());
            entities.add(entity);
        }
        return transferJpaRepository.saveAll(entities);
    }

    public Object updateTransfer(List<TransferRequest> requests, Integer reportId, Integer groupId) {
        List<TransferEntity> entities = new ArrayList<>();
        for (TransferRequest transfer : requests) {
            TransferEntity entity = new TransferEntity();
            if (transfer.getTransferId() == 0) {
                return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "transferId not exits", null);
            }
            if (groupId != null && groupId == transfer.getGroupId()) {
                return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "groupId is not crrurent groupId user", null);
            }
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

    public List<TransferResponseGroup> findGroupIdAndTransferDate(int groupId) {
        List<TransferEntity> entities = transferJpaRepository.findGroupIdAndTransferDate(groupId, DateUtils.formatDate(new Date()));
        List<TransferResponseGroup> transferResponseGroups = new ArrayList<>();
        for (TransferEntity entity : entities) {
            Optional<GroupEntity> groupEntity = groupJpaRepository.findById(entity.getGroupId());
            transferResponseGroups.add(new TransferResponseGroup(entity.getGroupId(), groupEntity.get().getGroupName(), entity.getTransferNum()));
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
