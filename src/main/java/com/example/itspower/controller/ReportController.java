package com.example.itspower.controller;
import com.example.itspower.exception.GeneralException;
import com.example.itspower.request.EmployeeGroupRequest;
import com.example.itspower.request.ReportRequest;
import com.example.itspower.request.RestRequestDelete;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public Object report(@RequestParam("reportDate") String reportDate, @RequestParam("groupId") int groupId) throws ParseException {
        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(reportDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // yourDate là thời gian hiện tại của bạn
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
        Date newDate = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(newDate);
        return reportService.reportDto(strDate, groupId);
    }
    @GetMapping("/getReportByYesterday")
    public Object getReportByYesterday(@RequestParam("reportDate") String reportDate, @RequestParam("groupId") int groupId) throws ParseException {
        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(reportDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // yourDate là thời gian hiện tại của bạn
        calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
        Date newDate = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(newDate);
        return reportService.callDataByDate(strDate, groupId);
    }
    @PostMapping("/report/save")
    public ResponseEntity<Object> save(@RequestBody ReportRequest reportRequest, @RequestParam("groupId") int groupId) throws GeneralException {
        try {
            return ResponseEntity.ok(reportService.save(reportRequest, groupId));
        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }
    }

    @PostMapping("/report/update")
    public ResponseEntity<Object> update(@RequestBody ReportRequest reportRequest, @RequestParam("groupId") int groupId) throws GeneralException {
        try {
            return ResponseEntity.ok(reportService.update(reportRequest, groupId));
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
}
