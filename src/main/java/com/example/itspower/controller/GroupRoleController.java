package com.example.itspower.controller;

import com.example.itspower.exception.ReasonException;
import com.example.itspower.request.GroupRoleRequest;
import com.example.itspower.response.BaseResponse;
import com.example.itspower.service.GroupRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.itspower.component.enums.StatusReason.ERROR;
import static com.example.itspower.component.enums.StatusReason.SUCCESS;

@RestController
public class GroupRoleController {
    @Autowired
    private GroupRoleService groupRoleService;

    @GetMapping("/groupRole")
    @CrossOrigin
    public ResponseEntity<BaseResponse<Object>> searchAll() {
        try {
            BaseResponse<Object> res = new BaseResponse<>(HttpStatus.CREATED.value(), SUCCESS, groupRoleService.searchAll());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @GetMapping("/groupRoleDeleteTm")
    @CrossOrigin
    public ResponseEntity<BaseResponse<Object>> searchAllDeleteTm() {
        try {
            BaseResponse<Object> res = new BaseResponse<>(HttpStatus.CREATED.value(), SUCCESS, groupRoleService.searchAllDeleteTM());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @GetMapping("/groupRole/view-root")
    @CrossOrigin
    public ResponseEntity<BaseResponse<Object>> count(@RequestParam("reportDate") String reportDate) {
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(reportDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date); // yourDate là thời gian hiện tại của bạn
            calendar.add(Calendar.HOUR_OF_DAY, 7); // thêm 7 giờ vào thời gian hiện tại
            Date newDate = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(newDate);
            BaseResponse<Object> res = new BaseResponse<>(HttpStatus.CREATED.value(), SUCCESS, groupRoleService.count(strDate));
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }


    @GetMapping("/getName")
    @CrossOrigin
    public ResponseEntity<BaseResponse<Object>> getName() {
        try {
            BaseResponse<Object> res = new BaseResponse<>(HttpStatus.CREATED.value(), SUCCESS, groupRoleService.getName());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @PostMapping("/groupRole/save")
    @CrossOrigin
    public ResponseEntity<Object> save(@RequestBody GroupRoleRequest groupRoleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupRoleService.save(groupRoleRequest));
    }

    @GetMapping("/groupRoleDetails")
    @CrossOrigin
    public ResponseEntity<Object> searchDetails(@Param("parentId") int parentId) {
        return ResponseEntity.status(HttpStatus.OK).body(groupRoleService.searchAllByParentId(parentId));
    }

    @GetMapping("/groupRoleRoot")
    @CrossOrigin
    public ResponseEntity<Object> searchRoleRoot() {
        return ResponseEntity.status(HttpStatus.OK).body(groupRoleService.searchAllByParentIdIsNull());
    }

    @GetMapping("/groupRole/demarcation")
    @CrossOrigin
    public ResponseEntity<Object> getByDemarcation(@RequestParam("groupId") Integer groupId) {
        try {
            return ResponseEntity.ok(groupRoleService.getDemarcationRes(groupId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/groupRole/getAllDemarcation")
    @CrossOrigin
    public ResponseEntity<Object> getAllDemarcation(@RequestParam(defaultValue = "1") Integer pageNo,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "groupName",required = false,defaultValue = "") String groupName) {
        try {
            return ResponseEntity.ok(groupRoleService.getAllDamercation(groupName, pageSize, pageNo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/groupRole/update")
    @CrossOrigin
    public ResponseEntity<Object> update(@RequestParam("id") Integer id, @RequestParam("demarcation") Integer demarcation) {
        try {
            return ResponseEntity.ok(groupRoleService.updateGroupRole(id, demarcation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/groupRole/delete")
    @CrossOrigin
    public ResponseEntity<Object> delete(@RequestParam("groupId") Integer groupId) {
        try {
            groupRoleService.delete(groupId);
            return ResponseEntity.ok("Thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @GetMapping("/groupRole/view-root")
//    @CrossOrigin
//    public ResponseEntity<Object> viewRoot() {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(groupRoleService.getViewRoot());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

}
