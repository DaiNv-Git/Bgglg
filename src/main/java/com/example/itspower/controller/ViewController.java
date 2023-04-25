package com.example.itspower.controller;

import com.example.itspower.exception.ReasonException;
import com.example.itspower.response.BaseResponse;
import com.example.itspower.service.ViewDetailService;
import com.example.itspower.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.itspower.component.enums.StatusReason.ERROR;
import static com.example.itspower.component.enums.StatusReason.SUCCESS;

@RestController
@RequestMapping("/view")
public class ViewController {
    @Autowired
    ViewService viewService;

    @Autowired
    private ViewDetailService viewDetailService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam("date") String date) {
        return ResponseEntity.status(HttpStatus.OK).body(viewService.getView(date));
    }

    @GetMapping("/groupRoleViewDetails")
    public ResponseEntity<BaseResponse<Object>> searchAllViewDetails(@RequestParam("reportDate") String reportDate) {
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(reportDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date); // yourDate là thời gian hiện tại của bạn
            calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
            Date newDate = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(newDate);
            BaseResponse<Object> res = new BaseResponse<>(HttpStatus.CREATED.value(),
                    SUCCESS, viewDetailService.searchAllView(strDate));
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @PostMapping("/exportExcel")
    public HttpServletResponse exportExcel(@RequestParam("reportDate") String reportDate, HttpServletResponse response) {
        try {
            String fileName = "bgglg-" + reportDate + ".xlsx";
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename = " + fileName);
            InputStream inputStream = viewDetailService.exportExcel(reportDate).getInputStream();
            System.out.printf("=================="+viewDetailService.exportExcel(reportDate).getInputStream());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, len);
            }
            response.flushBuffer();
            return response;
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.NOT_FOUND.value(), ERROR, e);
        }
    }
}
