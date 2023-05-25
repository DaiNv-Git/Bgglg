package com.example.itspower.service.impl;

import com.example.itspower.model.entity.EmployeeGroupEntity;
import com.example.itspower.repository.repositoryjpa.EmployeeGroupRepository;
import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.employee.EmployeeExportExcel;
import com.example.itspower.response.employee.EmployeeGroupResponse;
import com.example.itspower.service.EmployeeGroupService;
import com.example.itspower.util.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EmployeeGroupServiceImpl implements EmployeeGroupService {
    @Autowired
    EmployeeGroupRepository groupRepository;

    @Override
    public void saveAll(List<addUserRequest> addUser) {
        List<EmployeeGroupEntity> save = new ArrayList<>();
        for (addUserRequest addUserRequest : addUser) {
            EmployeeGroupEntity employeeGroup = new EmployeeGroupEntity();
            employeeGroup.setId(addUserRequest.getId());
            List<EmployeeGroupEntity> entity = groupRepository.findLaborCode(addUserRequest.getLaborCode());
            if (entity.size() >0) {
                throw new RuntimeException("labor code duplicate");
            }
            employeeGroup.setGroupId(addUserRequest.getGroupId());
            employeeGroup.setLaborCode(addUserRequest.getLaborCode());
            employeeGroup.setName(addUserRequest.getName());
            employeeGroup.setCreateDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
            save.add(employeeGroup);
        }
        groupRepository.saveAll(save);
    }

    @Override
    public byte[] exportExcel() throws IOException {
        List<EmployeeExportExcel> exportExcels = groupRepository.getExcelEmployee();
        Workbook workbook = new XSSFWorkbook();
        // Tạo một trang mới
        Sheet sheet = workbook.createSheet("employee list");

        // Tạo tiêu đề cột
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tổ");
        headerRow.createCell(1).setCellValue("Tên");
        headerRow.createCell(2).setCellValue("mã");

        // Đổ dữ liệu từ danh sách đối tượng vào các dòng trong Excel
        int rowNum = 1;
        for (EmployeeExportExcel object : exportExcels) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(object.getGroupName());
            row.createCell(1).setCellValue(object.getName());
            row.createCell(2).setCellValue(object.getLabor());

        }

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi workbook vào ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Trả về mảng byte từ ByteArrayOutputStream
        return outputStream.toByteArray();
    }


    @Override
    public void delete(List<Integer> ids) {
        groupRepository.deleteAllById(ids);
    }

    @Override
    public PageResponse getEmployee(String groupName, Integer groupId, String laborCode, String employeeName, int pageSize, int pageNo) {
        int offset = pageSize * (pageNo - 1);
        int countEmployee = groupRepository.countEmployee();
        List<EmployeeGroupResponse> res = groupRepository.getEmployee(groupId, groupName, laborCode, employeeName, pageSize, offset);
        Pageable pageable = PageRequest.of(offset, pageSize);
        final Page<EmployeeGroupResponse> page = new PageImpl<>(res, pageable, 0);
        return new PageResponse<>(page, (long) countEmployee);
    }
}
