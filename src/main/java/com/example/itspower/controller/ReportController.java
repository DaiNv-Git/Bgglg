package com.example.itspower.controller;

import com.example.itspower.exception.GeneralException;
import com.example.itspower.request.EmployeeGroupRequest;
import com.example.itspower.request.ReportRequest;
import com.example.itspower.request.RestRequestDelete;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public Object report(@RequestParam("reportDate") String reportDate, @RequestParam("groupId") int groupId) throws ParseException {
        try{
            LocalDate localDate = LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            LocalDateTime localDateTime = localDate.atStartOfDay().plusHours(7);
            String strDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return ResponseEntity.ok(reportService.search(strDate, groupId));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @CrossOrigin
    @GetMapping("/search")
    public Object search(@RequestParam("reportDate") String reportDate, @RequestParam("groupId") int groupId) throws ParseException {
        try{
            LocalDate localDate = LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            LocalDateTime localDateTime = localDate.atStartOfDay().plusHours(7);
            String strDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return ResponseEntity.ok(reportService.search(strDate, groupId));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/getReportByYesterday")
    public Object getReportByYesterday(@RequestParam("groupId") int groupId) {
        return reportService.callDataByDate(groupId);
    }
    @PostMapping("/report/save")
    public ResponseEntity<Object> save(@RequestBody ReportRequest reportRequest, @RequestParam("groupId") int groupId) throws GeneralException {
        try {
            reportService.save(reportRequest, groupId);
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK.value(), "add new success"));
        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }
    }

    @PostMapping("/report/update")
    public ResponseEntity<Object> update(@RequestBody ReportRequest reportRequest, @RequestParam("groupId") int groupId) throws GeneralException {
        try {
            reportService.update(reportRequest, groupId);
             return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK.value(), "add new success"));
        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }
    }

    @PostMapping("/report/delete-rest")
    public ResponseEntity<Object> deleteRest(@RequestBody RestRequestDelete reportRequest) {
        reportService.deleteRestIdsAndReportId(reportRequest.getReportId(), reportRequest.getRestIds());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK.value(), "delete success"));
    }

    @PostMapping("/report/delete-group-emp")
    public ResponseEntity<Object> deleteGroupEmp(@RequestBody EmployeeGroupRequest groupRequest) {
        reportService.deleteRestEmployee(groupRequest.getGroupId(), groupRequest.getLaborEmp());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK.value(), "delete success"));
    }

    @GetMapping("/getTransfer")
    public ResponseEntity<Object> getTransfer(@RequestParam("date") String reportDate, @RequestParam("groupID") Integer groupID) throws ParseException {
        LocalDate localDate = LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDateTime localDateTime = localDate.atStartOfDay().plusHours(7);
        String strDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getTransfer(strDate, groupID));
    }

    @GetMapping("/getIdsTomay")
    public ResponseEntity<Object> getID() throws ParseException {

        return ResponseEntity.status(HttpStatus.OK).body(reportService.getIdsToMay());
    }

}
