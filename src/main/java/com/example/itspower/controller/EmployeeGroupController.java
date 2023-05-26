package com.example.itspower.controller;

import com.example.itspower.exception.GeneralException;
import com.example.itspower.exception.ReasonException;
import com.example.itspower.request.search.SearchEmployeeRequest;
import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.BaseResponse;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.service.EmployeeGroupService;
import com.example.itspower.service.impl.ImportEmployee;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.itspower.component.enums.StatusReason.ERROR;
import static com.example.itspower.component.enums.StatusReason.SUCCESS;

@RestController
@RequestMapping("/employee")
public class EmployeeGroupController {
    @Autowired
    EmployeeGroupService employeeGroupService;
    @Autowired
    ImportEmployee importEmployee;

    @PostMapping("/save")
    public SuccessResponse save(@RequestBody List<addUserRequest> addUser) {
        try {
            employeeGroupService.saveAll(addUser);
            return new SuccessResponse<>(HttpStatus.CREATED.value(), "add new success", null);
        } catch (Exception e) {
                 return  new SuccessResponse<>(HttpStatus.BAD_REQUEST.value(), "laborCode is duplicate", null);
        }
    }

    @PostMapping("/update")
    public SuccessResponse update(@RequestBody List<addUserRequest> addUser) {
        try {
            employeeGroupService.saveAll(addUser);
            return new SuccessResponse<>(HttpStatus.CREATED.value(), "add new success", null);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @DeleteMapping("/delete")
    public SuccessResponse delete(@RequestBody List<Integer> ids) {
        try {
            employeeGroupService.delete(ids);
            return new SuccessResponse<>(HttpStatus.CREATED.value(), "delete success", null);
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.BAD_REQUEST.value(), ERROR, e);
        }
    }

    @PostMapping("/getEmployee")
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

    @PostMapping("/import")
    public ResponseEntity<Object> importExcel(@RequestParam MultipartFile file) throws IOException, GeneralException {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if (!extension.equalsIgnoreCase("xlsx") && !extension.equalsIgnoreCase("xls")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail" +
                        "The file is not in the correct excel format!");
            }
            importEmployee.getSheetFileExcel(file);
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.CREATED.value(), "ok"));
        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }
    }
    @GetMapping("/exportExcel")
    public ResponseEntity<Object> exportExcel() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(employeeGroupService.exportExcel()));
        } catch (Exception e) {
            throw new ReasonException(HttpStatus.NOT_FOUND.value(), "saiiiiii", "saiiiiii");
        }
    }
}
