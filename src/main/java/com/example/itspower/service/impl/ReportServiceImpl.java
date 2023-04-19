package com.example.itspower.service.impl;

import com.example.itspower.component.util.DateUtils;
import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.entity.ReportEntity;
import com.example.itspower.model.entity.RiceEntity;
import com.example.itspower.model.entity.TransferEntity;
import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.model.resultset.RestDto;
import com.example.itspower.repository.*;
import com.example.itspower.request.ReportRequest;
import com.example.itspower.request.TransferRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.report.ReportResponse;
import com.example.itspower.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private RestRepository restRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private GroupRoleRepository groupRoleRepository;
    @Autowired
    private RiceRepository riceRepository;

    @Override
    public Object reportDto(String reportDate, int groupId) {
        ReportDto reportDto = reportRepository.reportDto(reportDate, groupId);
        List<RestDto> restDtos = restRepository.getRests(reportDto.getId());
        List<TransferEntity> transferEntities = transferRepository.findByReportId(reportDto.getId());
        for (TransferEntity transfer : transferEntities) {
            Optional<GroupEntity> groupEntity = groupRoleRepository.findById(transfer.getGroupId());
            if (groupEntity.isPresent()) {
                transfer.setParentId(groupEntity.get().getParentId());
                transfer.setGroupName(groupEntity.get().getGroupName());
            }
        }
        RiceEntity riceEntity = riceRepository.getByRiceDetail(reportDto.getId());
        return new ReportResponse(reportDto, riceEntity, restDtos, transferEntities);
    }

    @Override
    public Object save(ReportRequest request, int groupId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
//        Date newDate = calendar.getTime();
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
        for (TransferRequest transferRequests : request.getTransferRequests()) {
            if (transferRequests.getTransferId() == 0) {
                return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "transferId not exits", null);
            }
            if (transferRequests.getGroupId() != null) {
                if (groupId == transferRequests.getGroupId().intValue()) {
                    return new SuccessResponse<>(HttpStatus.BAD_REQUEST.value(), "group name not current group user!", null);
                }
            }
        }
        boolean isCheck = check(request.getRiceRequests().getRiceCus(), request.getRiceRequests().getRiceEmp(), request.getRiceRequests().getRiceVip());
        if (isCheck) {
            return new SuccessResponse<>(HttpStatus.BAD_REQUEST.value(), "rise < 0", null);
        }
        boolean isCheckReport = checkReport(request);
        if (isCheckReport) {
            return new SuccessResponse<>(HttpStatus.BAD_REQUEST.value(), "partTimeNum or studentNum <0", null);
        }
        boolean isCheckTransfer = checkTransfer(request.getTransferRequests());
        if (isCheckTransfer) {
            return new SuccessResponse<>(HttpStatus.BAD_REQUEST.value(), "TransferNum <0", null);
        }
        ReportEntity reportEntity = reportRepository.updateReport(request, groupId);
        riceRepository.updateRice(request.getRiceRequests(), reportEntity.getId());
        restRepository.updateRest(request.getRestRequests(), reportEntity.getId());
        transferRepository.updateTransfer(request.getTransferRequests(), reportEntity.getId());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK.value(), "update report success", reportDto(DateUtils.formatDate(reportEntity.getReportDate()), reportEntity.getGroupId())));
    }

    @Override
    public void deleteRestIdsAndReportId(Integer reportId, List<Integer> restIds) {
        restRepository.deleteRestIdsAndReportId(reportId, restIds);
        // report id
    }

    private boolean check(int riceCus, int riseEmp, int riseVip) {
        if (riceCus < 0 || riseEmp < 0 || riseVip < 0) {
            return true;
        }
        return false;
    }

    private boolean checkReport(ReportRequest request) {
        if (request.getPartTimeNum() < 0 || request.getStudentNum() < 0) {
            return true;
        }
        return false;
    }

    private boolean checkTransfer(List<TransferRequest> request) {
        if (request.get(0).getTransferNum() < 0 || request.get(1).getTransferNum() < 0) {
            return true;
        }
        return false;
    }
}