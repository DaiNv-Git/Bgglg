package com.example.itspower.controller;

import com.example.itspower.exception.ReasonException;
import com.example.itspower.request.search.SearchEmployeeRequest;
import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.BaseResponse;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.service.EmployeeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.itspower.component.enums.StatusReason.ERROR;
import static com.example.itspower.component.enums.StatusReason.SUCCESS;

@RestController
@RequestMapping("/employee")
public class EmployeeGroupController {
    @Autowired
    EmployeeGroupService employeeGroupService;

    @PostMapping("/save")
    @CrossOrigin
    public SuccessResponse save(@RequestBody List<addUserRequest> addUser) {
        try {
            employeeGroupService.saveAll(addUser);
            return new SuccessResponse<>(HttpStatus.CREATED.value(), "add new success", null);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @PostMapping("/update")
    @CrossOrigin
    public SuccessResponse update(@RequestBody List<addUserRequest> addUser) {
        try {
            employeeGroupService.saveAll(addUser);
            return new SuccessResponse<>(HttpStatus.CREATED.value(), "add new success", null);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @DeleteMapping("/delete")
    @CrossOrigin
    public SuccessResponse delete(@RequestBody List<Integer> ids) {
        try {
            employeeGroupService.delete(ids);
            return new SuccessResponse<>(HttpStatus.CREATED.value(), "delete success", null);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @PostMapping("/getEmployee")
    @CrossOrigin
    public ResponseEntity<BaseResponse<Object>> searchAllViewDetails(@RequestBody Optional<SearchEmployeeRequest> searchForm,
                                                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(defaultValue = "5") Integer pageSize) {
        try {
            SearchEmployeeRequest forms = searchForm.orElse(new SearchEmployeeRequest());
            BaseResponse<Object> res = new BaseResponse<>(HttpStatus.CREATED.value(),
                    SUCCESS, employeeGroupService.getEmployee(forms.getGroupName(), forms.getGroupId(), forms.getLaborCode(), forms.getEmployeeName(), pageSize, pageNo));
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }
}
